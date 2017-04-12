/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ranger.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.log4j.Logger;
import org.apache.ranger.common.RangerCommonEnums;
import org.apache.ranger.common.db.BaseDao;
import org.apache.ranger.entity.XXGroupPermission;

public class XXGroupPermissionDao extends BaseDao<XXGroupPermission> {

	private static final Logger logger = Logger.getLogger(XXGroupPermissionDao.class);

	public XXGroupPermissionDao(RangerDaoManagerBase daoManager) {
		super(daoManager);
	}

	public List<XXGroupPermission> findByModuleId(Long moduleId,
			boolean isUpdate) {
		if (moduleId != null) {
			try {
				if (isUpdate) {
					return getEntityManager()
							.createNamedQuery(
									"XXGroupPermissionUpdate.findByModuleId",
									XXGroupPermission.class)
							.setParameter("moduleId", moduleId).getResultList();
				}
				return getEntityManager()
						.createNamedQuery(
								"XXGroupPermissionUpdates.findByModuleId",
								XXGroupPermission.class)
						.setParameter("moduleId", moduleId)
						.setParameter("isAllowed", RangerCommonEnums.IS_ALLOWED)
						.getResultList();
			} catch (NoResultException e) {
				logger.debug(e.getMessage());
			}
		} else {
			logger.debug("ResourcegropuIdId not provided.");
			return new ArrayList<XXGroupPermission>();
		}
		return null;
	}

	public List<XXGroupPermission> findByGroupId(Long groupId) {
		if (groupId != null) {
			try {
				return getEntityManager()
						.createNamedQuery(
								"XXGroupPermission.findByGroupId",
								XXGroupPermission.class)
						.setParameter("groupId", groupId).getResultList();
			} catch (NoResultException e) {
				logger.debug(e.getMessage());
			}
		} else {
			logger.debug("ResourcegropuIdId not provided.");
			return new ArrayList<XXGroupPermission>();
		}
		return null;
	}
	public List<XXGroupPermission> findbyVXPortalUserId(Long userId) {
		if (userId != null) {
			try {
				return getEntityManager()
						.createNamedQuery(
								"XXGroupPermission.findByVXPoratUserId",
								XXGroupPermission.class)
						.setParameter("userId", userId)
						.setParameter("isAllowed", RangerCommonEnums.IS_ALLOWED)
						.getResultList();
			} catch (NoResultException e) {
				logger.debug(e.getMessage());
			}
		} else {
			logger.debug("ResourcegropuIdId not provided.");
			return new ArrayList<XXGroupPermission>();
		}
		return null;
	}

	public XXGroupPermission findByModuleIdAndGroupId(Long groupId, Long moduleId) {
		if (groupId != null && moduleId != null) {
			try {
				return getEntityManager().createNamedQuery("XXGroupPermission.findByModuleIdAndGroupId", tClass).setParameter("groupId", groupId).setParameter("moduleId", moduleId)
						.getSingleResult();
			} catch (NoResultException e) {
				logger.debug(e.getMessage());
			}
		} else {
			return null;
		}
		return null;
	}
	public void deleteByModuleId(Long moduleId) {
		if (moduleId != null) {
			try {
				getEntityManager()
					.createNamedQuery("XXGroupPermission.deleteByModuleId", XXGroupPermission.class)
					.setParameter("moduleId", moduleId)
					.executeUpdate();
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		} else {
			logger.debug("ModuleId not provided.");
		}
	}

}
