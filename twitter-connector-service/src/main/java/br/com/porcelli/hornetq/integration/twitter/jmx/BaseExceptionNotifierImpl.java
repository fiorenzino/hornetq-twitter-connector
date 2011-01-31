/**
 * Copyright (c) 2010 Alexandre Porcelli <alexandre.porcelli@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.porcelli.hornetq.integration.twitter.jmx;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public abstract class BaseExceptionNotifierImpl extends NotificationBroadcasterSupport implements
    ExceptionNotifier {
    public static final String EXCEPTION = "org.hornetq.connector.service.twitter.exception";

    protected AtomicInteger    seqNum    = new AtomicInteger();

    public void notifyException(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.append("------\n");
        ex.printStackTrace(pw);
        pw.append("------\n");

        Notification event = new Notification(EXCEPTION, this, seqNum.getAndAdd(1), System.currentTimeMillis(), sw.toString());

        sendNotification(event);
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {

        String[] types = new String[] {
            EXCEPTION
        };
        String name = Notification.class.getName();
        String description = "HornetQ Twitter connector service exception notification";
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, name, description);

        return (new MBeanNotificationInfo[] {info});
    }
}
