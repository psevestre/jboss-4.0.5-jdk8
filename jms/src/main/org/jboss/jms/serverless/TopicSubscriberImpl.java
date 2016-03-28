/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jms.serverless;

import org.jboss.logging.Logger;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.jms.JMSException;
import javax.jms.MessageListener;

/**
 * 
 * @author Ovidiu Feodorov <ovidiu@jboss.org>
 * @version $Revision: 57195 $ $Date: 2006-09-26 08:08:17 -0400 (Tue, 26 Sep 2006) $
 *
 **/
class TopicSubscriberImpl extends MessageConsumerImpl implements TopicSubscriber {

    private static final Logger log = Logger.getLogger(TopicSubscriberImpl.class);

    TopicSubscriberImpl(SessionImpl session, Topic topic) {

        super(session, topic);
    }

    //
    // MessageConsumer INTERFACE METHODS
    //

    public void close() throws JMSException {
        throw new NotImplementedException();
    }


    //
    // TopicSubscriber INTERFACE IMPLEMENTATION
    //

    public Topic getTopic() throws JMSException {
        return (Topic)getDestination();
    }

    public boolean getNoLocal() throws JMSException {
        throw new NotImplementedException();
    }
}
