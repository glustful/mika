/**
 *
 * Copyright 2003-2007 Jive Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yxst.epic.yixin.push.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import com.yxst.epic.yixin.push.cli.PushMessage;

/**
 * Implements the logical OR operation over two or more packet filters. In
 * other words, packets pass this filter if they pass <b>any</b> of the filters.
 *
 * @author Matt Tucker
 */
public class OrFilter implements PushMessageFilter {

    /**
     * The list of filters.
     */
    private final List<PushMessageFilter> filters;

    /**
     * Creates an empty OR filter. Filters should be added using the
     * {@link #addFilter(PacketFilter)} method.
     */
    public OrFilter() {
        filters = new ArrayList<PushMessageFilter>();
    }

    /**
     * Creates an OR filter using the specified filters.
     *
     * @param filters the filters to add.
     */
    public OrFilter(PushMessageFilter... filters) {
        if (filters == null) {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        for(PushMessageFilter filter : filters) {
            if(filter == null) {
                throw new IllegalArgumentException("Parameter must not be null.");
            }
        }
        this.filters = new ArrayList<PushMessageFilter>(Arrays.asList(filters));
    }

    /**
     * Adds a filter to the filter list for the OR operation. A packet
     * will pass the filter if any filter in the list accepts it.
     *
     * @param filter a filter to add to the filter list.
     */
    public void addFilter(PushMessageFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        filters.add(filter);
    }

    public String toString() {
        return filters.toString();
    }

	@Override
	public boolean acceptOnlineMessage(PushMessage message) {
		for (PushMessageFilter filter : filters) {
            if (filter.acceptOnlineMessage(message)) {
                return true;
            }
        }
		return false;
	}

	@Override
	public boolean acceptOfflineMessage(ArrayList<PushMessage> messages) {
		
		ArrayList<PushMessage> list = getAcceptOfflineMessage(messages);
		
		if (list != null && list.size() > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public ArrayList<PushMessage> getAcceptOfflineMessage(
			ArrayList<PushMessage> messages) {
		
		ArrayList<PushMessage> list = new ArrayList<PushMessage>();
		
		for (PushMessageFilter filter : filters) {
			ArrayList<PushMessage> flist = filter.getAcceptOfflineMessage(messages);
			list.addAll(flist);
		}
		
		return new ArrayList<PushMessage>(new LinkedHashSet<PushMessage>(list));
	}
}
