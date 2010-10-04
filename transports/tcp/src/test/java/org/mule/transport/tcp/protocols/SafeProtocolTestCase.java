/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.tcp.protocols;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.DynamicPortTestCase;

public class SafeProtocolTestCase extends DynamicPortTestCase
{

    protected static String TEST_MESSAGE = "Test TCP Request";

    protected String getConfigResources()
    {
        return "safe-protocol-test.xml";
    }

    public void testSafeToSafe() throws MuleException
    {
        MuleClient client = new MuleClient(muleContext);
        assertResponseOk(client.send("tcp://localhost:" + getPorts().get(0) + "?connector=safe", TEST_MESSAGE, null));
    }

    public void testUnsafeToSafe() throws MuleException
    {
        MuleClient client = new MuleClient(muleContext);
        assertResponseBad(client.send("tcp://localhost:" + getPorts().get(0) + "?connector=unsafe", TEST_MESSAGE, null));
    }

    private void assertResponseOk(MuleMessage message)
    {
        assertNotNull("Null message", message);
        Object payload = message.getPayload();
        assertNotNull("Null payload", payload);
        assertTrue("Payload not byte[]", payload instanceof byte[]);
        assertEquals(TEST_MESSAGE + " Received", new String((byte[]) payload));
    }

    protected void assertResponseBad(MuleMessage message)
    {
        try
        {
            if (message.getPayloadAsString().equals(TEST_MESSAGE + " Received"))
            {
                fail("expected error");
            }
        }
        catch (Exception e)
        {
            // expected
        }
    }

    @Override
    protected int getNumPortsToFind()
    {
        return 2;
    }

}
