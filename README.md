About
=====

This is a Twitter Connector service for [HornetQ](http://jboss.org/hornetq). It allows you interact with twitter using HornetQ, wich gives you flexibility and scalability out of the box.

There are two types of twitter connectors, outgoing and stream. Outgoing connector consumes from a configurable address and forwards to twitter. Stream connector consumes from twitter and forwards to a configurable address.

Why use HornetQ to interact with Twitter?
=====



How to Use
=====

 You just need to add some additional lines on `hornetq-configuration.xml` and `hornetq-jms.xml`. Here you have a sample config that uses the stream connector.

 hornetq-configuration.xml:

	[...]
	<address-settings>
		[...]
		<address-setting match="jms.queue.lastTweetQueueLL">
			<last-value-queue>true</last-value-queue>
		</address-setting>
		<address-setting match="jms.queue.lastTweetQueueDMZ">
			<last-value-queue>true</last-value-queue>
		</address-setting>
	</address-settings>
	[...]
	<connector-services>
		[...]
		<connector-service name="twitter-streamming">
			<factory-class>br.com.porcelli.hornetq.integration.twitter.TwitterStreamConnectorServiceFactory</factory-class>
			<param key="queue" value="jms.queue.incomingQueueFilter"/>
			<param key="lastTweetQueue" value="jms.queue.lastTweetQueue"/>
			<param key="lastDMQueue" value="jms.queue.lastTweetQueueDM"/>
			<param key="consumerKey" value="******************"/>
			<param key="consumerSecret" value="*****************************************"/>
			<param key="accessToken" value="**************************************************"/>
			<param key="accessTokenSecret" value="******************************************"/>
			<param key="screenName" value="porcelli"/>
			<param key="streamListeners" value="br.com.porcelli.hornetq.integration.twitter.stream.listener.impl.TwitterUserStreamSimpleListener"/>
			<param key="tweetReclaimers" value="br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl.ReclaimLostMentionedList;br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl.ReclaimLostUserMentions;br.com.porcelli.hornetq.integration.twitter.stream.reclaimer.impl.ReclaimLostDirectMessages"/>
		</connector-service>
	</connector-services>
	[...]

  hornetq-jms.xml:

	[...]
	<queue name="lastTweetQueue">
		<entry name="/queue/lastTweetQueue"/>
	</queue>

	<queue name="lastTweetQueueDM">
		<entry name="/queue/lastTweetQueueDM"/>
	</queue>

	<queue name="incomingQueueFilter">
		<entry name="/queue/incomingQueueFilter"/>
	</queue>
	[...]

Configuration
=====

Stream Connector
-----

Outgoing Connector
-----


Extensions on Stream Connector
=====



Consuming Stream Tweets
=====



Sending Tweets
=====




How to Install
=====

All you need is run `mvn clean package` and than copy the generated files to ... 

Issue Tracking
=====

<https://github.com/porcelli/hornetq-twitter-streaming/issues>

Supported Version:
=====

* [HornetQ version 2.1.2 Final](http://jboss.org/hornetq)

External Dependencies:
-----

* [Twitter4J version 2.1.8](http://twitter4j.org)


Know Issues
=====



License
=====

(The Apache License, Version 2.0)

Copyright (c) 2010 Alexandre Porcelli [alexandre.porcelli@gmail.com]

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at:

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

