package org.ihtsdo.otf.tcc.model.cs;

import gov.vha.isaac.ochre.collections.NidSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.ihtsdo.otf.tcc.model.cc.PersistentStore;
import org.ihtsdo.otf.tcc.model.cc.concept.ConceptChronicle;
import org.ihtsdo.otf.tcc.api.time.TimeHelper;
import org.ihtsdo.otf.tcc.api.concept.ConceptFetcherBI;
import org.ihtsdo.otf.tcc.api.nid.NativeIdSetBI;
import org.ihtsdo.otf.tcc.api.nid.NidSetBI;
import org.ihtsdo.otf.tcc.api.concept.ProcessUnfetchedConceptDataBI;
import org.ihtsdo.otf.tcc.api.changeset.ChangeSetGenerationPolicy;
import org.ihtsdo.otf.tcc.api.changeset.ChangeSetGeneratorBI;
import org.ihtsdo.otf.tcc.api.cs.ChangeSetWriterThreading;

public class ChangeSetWriterHandler implements Runnable, ProcessUnfetchedConceptDataBI {

   private static final ConcurrentHashMap<String, ChangeSetGeneratorBI> writerMap = new ConcurrentHashMap<>();
   public static AtomicInteger changeSetWriters = new AtomicInteger();
   private final NidSet cNidsToWrite;
   private final long commitTime;
   private final String commitTimeStr;
   private final NidSetBI sapNidsFromCommit;
   private final long startTime = System.currentTimeMillis();
   private final AtomicInteger processedCount = new AtomicInteger();
   private final AtomicInteger processedChangedCount = new AtomicInteger();
   private int changedCount = Integer.MIN_VALUE;
   private final ChangeSetWriterThreading changeSetWriterThreading;
   private final ChangeSetGenerationPolicy changeSetPolicy;
   private final List<ChangeSetGeneratorBI> writerListForHandler;

   public ChangeSetWriterHandler(NidSet cNidsToWrite,
           long commitTime, NidSetBI sapNidsFromCommit, ChangeSetGenerationPolicy changeSetPolicy,
           ChangeSetWriterThreading changeSetWriterThreading) {
      super();
      assert commitTime != Long.MAX_VALUE;
      assert commitTime != Long.MIN_VALUE;
      this.cNidsToWrite = cNidsToWrite;
      changedCount = cNidsToWrite.size();
      this.commitTime = commitTime;
      this.commitTimeStr = TimeHelper.formatDate(commitTime)
              + " (" + cNidsToWrite.size() + " concepts)";
      this.sapNidsFromCommit = sapNidsFromCommit;
      this.changeSetWriterThreading = changeSetWriterThreading;
      changeSetWriters.incrementAndGet();
      this.changeSetPolicy = changeSetPolicy;
      writerListForHandler = new ArrayList<>(writerMap.values());
   }

   @Override
   public void run() {
      try {

         for (ChangeSetGeneratorBI writer : writerListForHandler) {
            writer.open(sapNidsFromCommit);
         }
         switch (changeSetWriterThreading) {
            case MULTI_THREAD:
               PersistentStore.get().iterateConceptDataInParallel(this);
               break;
            case SINGLE_THREAD:
               PersistentStore.get().iterateConceptDataInSequence(this);
               break;
            default:
               throw new RuntimeException("Can't handle threading: " + changeSetWriterThreading);
         }

         for (ChangeSetGeneratorBI writer : writerListForHandler) {
            writer.commit();
         }
         long endTime = System.currentTimeMillis();

         long elapsed = endTime - startTime;
         String elapsedStr = TimeHelper.getElapsedTimeString(elapsed);

       } catch (Exception e) {
         ChangeSetLogger.logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
      } 
   }

   public static void addWriter(String key, ChangeSetGeneratorBI writer) {
      writerMap.put(key, writer);
   }

   public static void removeWriter(String key) {
      writerMap.remove(key);
   }

   @Override
   public void processUnfetchedConceptData(int cNid, ConceptFetcherBI fcfc) throws Exception {
      if (cNidsToWrite.contains(cNid)) {
         processedChangedCount.incrementAndGet();
         ConceptChronicle c = (ConceptChronicle) fcfc.fetch();
         for (ChangeSetGeneratorBI writer : writerListForHandler) {
            writer.setPolicy(changeSetPolicy);
            writer.writeChanges(c, commitTime);
         }
      }
   }

   @Override
   public boolean continueWork() {
      // user cannot cancel operation
      return true;
   }

   @Override
   public NidSet getNidSet() {
      return cNidsToWrite;
   }

    @Override
    public boolean allowCancel() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Writing change sets";
    }
}
