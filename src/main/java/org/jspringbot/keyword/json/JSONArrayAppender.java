/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.keyword.json;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONArrayAppender implements JSONAppender {

    private JSONArray jsonArray;

    private String name;

    public JSONArrayAppender() {
        this(null);
    }

    public JSONArrayAppender(String name) {
        this.name = name;
        jsonArray = new JSONArray();
    }

    public String getName() {
        return name;
    }

    @Override
    public void append(Object obj) throws JSONException {
        jsonArray.put(obj);
    }

    @Override
    public void append(String name, Object obj) throws JSONException {
        throw new UnsupportedOperationException("Cannot add property on an array.");
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    @Override
    public String toString() {
        return jsonArray.toString();
    }
}
