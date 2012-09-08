/**
 * Copyright [2011] [PagSeguro Internet Ltda.]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package br.com.uol.pagseguro.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class UrlUtil {

    public static String buildQueryString(Map parameterMap) {
        Set entrySet = parameterMap.entrySet();
        Iterator entrySetIterator = entrySet.iterator();

        if (!entrySetIterator.hasNext()) {
            return "";
        }

        StringBuffer sb = new StringBuffer(256);
        Map.Entry mapEntry = (Map.Entry) entrySetIterator.next();
        sb.append(mapEntry.getKey());
        sb.append("=");
        sb.append(mapEntry.getValue());

        while (entrySetIterator.hasNext()) {
            mapEntry = (Map.Entry) entrySetIterator.next();
            sb.append("&");
            sb.append(mapEntry.getKey());
            sb.append("=");
            sb.append(mapEntry.getValue());
        }

        return sb.toString();
    }
}
