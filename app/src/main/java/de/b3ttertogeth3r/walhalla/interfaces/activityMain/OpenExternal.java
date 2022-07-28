/*
 * Copyright (c) 2022-2022.
 *
 * Licensed under the Apace License, Version 2.0 (the "Licence"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 *  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.b3ttertogeth3r.walhalla.interfaces.activityMain;


import de.b3ttertogeth3r.walhalla.object.Event;
import de.b3ttertogeth3r.walhalla.object.File;

/**
 * Interface to open external pages, files or apps.
 *
 * @author B3tterTogeth3r
 * @version 3.0
 * @
 * @since 1.0
 */
public interface OpenExternal {
    /**
     * Open a website in the devices default web-browser.
     *
     * @param link Link of the web site
     */
    void link(String link);

    /**
     * Send an email via the devices default email application.
     *
     * @param recipient Array of recipient email addresses.
     * @param subject   Subject of the email.
     */
    void sendEmail(String[] recipient, String subject);

    /**
     * Save an event in the devices default calendar application.
     *
     * @param event The {@link Event} to save in the calendar.
     */
    void saveInCalendar(Event event);

    /**
     * Open a {@link File} in the devices PDF or gallery.
     *
     * @param file The file to open.
     */
    void file(File file);

    /**
     * Open an image in the devices default gallery.
     *
     * @param link todo change this to something the device can open
     */
    void image(String link);
}
