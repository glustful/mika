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

import com.yxst.epic.yixin.push.cli.PushMessage;

/**
 * Implements the logical NOT operation on a packet filter. In other words,
 * packets pass this filter if they do not pass the supplied filter.
 *
 * @author Matt Tucker
 */
public class NotFilter implements PushMessageFilter {

	private PushMessageFilter filter;

	/**
	 * Creates a NOT filter using the specified filter.
	 *
	 * @param filter
	 *            the filter.
	 */
	public NotFilter(PushMessageFilter filter) {
		if (filter == null) {
			throw new IllegalArgumentException("Parameter must not be null.");
		}
		this.filter = filter;
	}

	@Override
	public boolean acceptOnlineMessage(PushMessage message) {
		return !filter.acceptOnlineMessage(message);
	}

	@Override
	public boolean acceptOfflineMessage(ArrayList<PushMessage> messages) {
		return !filter.acceptOfflineMessage(messages);
	}

	@Override
	public ArrayList<PushMessage> getAcceptOfflineMessage(
			ArrayList<PushMessage> messages) {

		ArrayList<PushMessage> list = new ArrayList<PushMessage>();

		ArrayList<PushMessage> fList = filter.getAcceptOfflineMessage(messages);
		if (fList != null) {
			for (PushMessage message : fList) {
				if (!messages.contains(message)) {
					list.add(message);
				}
			}
		}

		return list;
	}
}
