/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.plugin.policyengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class RangerAccessRequestImpl implements RangerAccessRequest {
	private static final Logger LOG = Logger.getLogger(RangerAccessRequestImpl.class);

	private RangerAccessResource resource        = null;
	private String               accessType      = null;
	private String               user            = null;
	private Set<String>          userGroups      = null;
	private Date                 accessTime      = null;
	private String               clientIPAddress = null;
	private List<String>         forwardedAddresses = null;
	private String               remoteIPAddress = null;
	private String               clientType      = null;
	private String               action          = null;
	private String               requestData     = null;
	private String               sessionId       = null;
	private Map<String, Object>  context         = null;

	private boolean isAccessTypeAny            = false;
	private boolean isAccessTypeDelegatedAdmin = false;
	private ResourceMatchingScope resourceMatchingScope = ResourceMatchingScope.SELF;

	public RangerAccessRequestImpl() {
		this(null, null, null, null);
	}

	public RangerAccessRequestImpl(RangerAccessResource resource, String accessType, String user, Set<String> userGroups) {
		setResource(resource);
		setAccessType(accessType);
		setUser(user);
		setUserGroups(userGroups);
		setForwardedAddresses(null);

		// set remaining fields to default value
		setAccessTime(null);
		setRemoteIPAddress(null);
		setClientType(null);
		setAction(null);
		setRequestData(null);
		setSessionId(null);
		setContext(null);
	}

	@Override
	public RangerAccessResource getResource() {
		return resource;
	}

	@Override
	public String getAccessType() {
		return accessType;
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public Set<String> getUserGroups() {
		return userGroups;
	}

	@Override
	public Date getAccessTime() {
		return accessTime;
	}

	@Override
	public String getClientIPAddress() { return clientIPAddress;}

	@Override
	public String getRemoteIPAddress() {
		return remoteIPAddress;
	}

	@Override
	public List<String> getForwardedAddresses() { return forwardedAddresses; }

	@Override
	public String getClientType() {
		return clientType;
	}

	@Override
	public String getAction() {
		return action;
	}

	@Override
	public String getRequestData() {
		return requestData;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public Map<String, Object> getContext() {
		return context;
	}

	@Override
	public ResourceMatchingScope getResourceMatchingScope() {
		return resourceMatchingScope;
	}

	@Override
	public boolean isAccessTypeAny() {
		return isAccessTypeAny;
	}

	@Override
	public boolean isAccessTypeDelegatedAdmin() {
		return isAccessTypeDelegatedAdmin;
	}

	public void setResource(RangerAccessResource resource) {
		this.resource = resource;
	}

	public void setAccessType(String accessType) {
		if (StringUtils.isEmpty(accessType)) {
			accessType = RangerPolicyEngine.ANY_ACCESS;
		}

		this.accessType            = accessType;
		isAccessTypeAny            = StringUtils.equals(accessType, RangerPolicyEngine.ANY_ACCESS);
		isAccessTypeDelegatedAdmin = StringUtils.equals(accessType, RangerPolicyEngine.ADMIN_ACCESS);
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setUserGroups(Set<String> userGroups) {
		this.userGroups = (userGroups == null) ? new HashSet<String>() : userGroups;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = (accessTime == null) ? new Date() : accessTime;
	}

	public void setClientIPAddress(String ipAddress) {
		this.clientIPAddress = ipAddress;
	}

	public void setForwardedAddresses(List<String> forwardedAddresses) {
		this.forwardedAddresses = (forwardedAddresses == null) ? new ArrayList<String>() : forwardedAddresses;
	}

	public void setRemoteIPAddress(String remoteIPAddress) {
		this.remoteIPAddress = remoteIPAddress;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setResourceMatchingScope(ResourceMatchingScope scope) { this.resourceMatchingScope = scope; }

	public void setContext(Map<String, Object> context) {
		this.context = (context == null) ? new HashMap<String, Object>() : context;
	}

	protected void extractAndSetClientIPAddress(boolean useForwardedIPAddress, String[]trustedProxyAddresses) {
		String ip = getRemoteIPAddress();
		if (ip == null) {
			ip = getClientIPAddress();
		}

		String newIp = ip;

		if (useForwardedIPAddress) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Using X-Forward-For...");
			}
			if (CollectionUtils.isNotEmpty(getForwardedAddresses())) {
				if (trustedProxyAddresses != null && trustedProxyAddresses.length > 0) {
					if (StringUtils.isNotEmpty(ip)) {
						for (String trustedProxyAddress : trustedProxyAddresses) {
							if (StringUtils.equals(ip, trustedProxyAddress)) {
								newIp = getForwardedAddresses().get(0);
								break;
							}
						}
					}
				} else {
					newIp = getForwardedAddresses().get(0);
				}
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("No X-Forwarded-For addresses in the access-request");
				}
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Old Remote/Client IP Address=" + ip + ", new IP Address=" + newIp);
		}
		setClientIPAddress(newIp);
	}

	@Override
	public String toString( ) {
		StringBuilder sb = new StringBuilder();

		toString(sb);

		return sb.toString();
	}

	public StringBuilder toString(StringBuilder sb) {
		sb.append("RangerAccessRequestImpl={");

		sb.append("resource={").append(resource).append("} ");
		sb.append("accessType={").append(accessType).append("} ");
		sb.append("user={").append(user).append("} ");

		sb.append("userGroups={");
		if(userGroups != null) {
			for(String userGroup : userGroups) {
				sb.append(userGroup).append(" ");
			}
		}
		sb.append("} ");

		sb.append("accessTime={").append(accessTime).append("} ");
		sb.append("clientIPAddress={").append(getClientIPAddress()).append("} ");
		sb.append("forwardedAddresses={").append(StringUtils.join(forwardedAddresses, " ")).append("} ");
		sb.append("remoteIPAddress={").append(remoteIPAddress).append("} ");
		sb.append("clientType={").append(clientType).append("} ");
		sb.append("action={").append(action).append("} ");
		sb.append("requestData={").append(requestData).append("} ");
		sb.append("sessionId={").append(sessionId).append("} ");
		sb.append("resourceMatchingScope={").append(resourceMatchingScope).append("} ");

		sb.append("context={");
		if(context != null) {
			for(Map.Entry<String, Object> e : context.entrySet()) {
				sb.append(e.getKey()).append("={").append(e.getValue()).append("} ");
			}
		}
		sb.append("} ");

		sb.append("}");

		return sb;
	}
	@Override
	public RangerAccessRequest getReadOnlyCopy() {
		return new RangerAccessRequestReadOnly(this);
	}
}
