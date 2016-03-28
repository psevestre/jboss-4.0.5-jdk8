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
package org.jboss.ejb3.test.service;

import javax.annotation.Resource;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;
import org.jboss.logging.Logger;

/**
 * @version <tt>$Revision: 45190 $</tt>
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 */
@Service(objectName = "jboss.ejb3.bugs:service=TestResourceInjectionService")
@Management(TestResourceInjectionServiceIF.class)
public class TestResourceInjectionService implements TestResourceInjectionServiceIF {

	private static Logger log = Logger.getLogger(TestResourceInjectionService.class);
	
	public boolean testedSuccessful = true;

	@Resource(mappedName = "topic/testTopic")
	private Topic testTopic;
	
	@Resource(mappedName = "ConnectionFactory")
	private TopicConnectionFactory topicConnectionFactory;
	
	public boolean getTestedSuccessful() {
		return testedSuccessful;
	}

// - Service life cycle --------------------------------------------------------
	
	public void create() throws Exception {
		log.info("TestResourceInjectionService.create()");
		testedSuccessful = testTopic != null && topicConnectionFactory != null;
	}
	
	public void start() throws Exception {
		log.info("TestResourceInjectionService.start()");
		if(testTopic == null)
        {
			log.warn("Dependent resource injection 'testTopic' failed");
            testedSuccessful = false;
        }
        
		if(topicConnectionFactory == null)
        {
			log.warn("Dependent resource injection 'topicConnectionFactory' failed");
            testedSuccessful = false;
        }
	}
	
	public void stop() {
		log.info("TestResourceInjectionService.stop()");
	}
	
	public void destroy() {
		log.info("TestResourceInjectionService.destroy()");
	}	
	
}
