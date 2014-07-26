/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ihtsdo.otf.tcc.rest.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import org.ihtsdo.otf.tcc.model.cc.PersistentStore;

/**
 *
 * @author kec
 */
@Path("chronicle/property")
public class PropertyResource {
        
    @GET
    @Produces("application/bdb")
    public StreamingOutput getProperties() throws IOException  {
        return new StreamingOutput() {

            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                ObjectOutputStream oos = new ObjectOutputStream(output);
                oos.writeObject(PersistentStore.get().getProperties());
            }
        };
    }

    @GET
    @Path("{key}")
    @Produces("text/plain")
    public String getProperty(@PathParam("key") String key) throws IOException  {
        return PersistentStore.get().getProperty(key);
    }

    
}
