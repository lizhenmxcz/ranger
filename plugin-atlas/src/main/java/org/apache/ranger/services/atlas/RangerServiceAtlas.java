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
package org.apache.ranger.services.atlas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ranger.plugin.model.RangerService;
import org.apache.ranger.plugin.model.RangerServiceDef;
import org.apache.ranger.plugin.service.RangerBaseService;
import org.apache.ranger.plugin.service.ResourceLookupContext;
import org.apache.ranger.services.atlas.client.AtlasResourceMgr;


public class RangerServiceAtlas extends RangerBaseService {

    private static final Log LOG = LogFactory.getLog(RangerServiceAtlas.class);

    public RangerServiceAtlas() {
        super();
    }

    @Override
    public void init(RangerServiceDef serviceDef, RangerService service) {
        super.init(serviceDef, service);
    }

    @Override
    public HashMap<String,Object> validateConfig() throws Exception {
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
            String serviceName = getServiceName();
                if(LOG.isDebugEnabled()) {
                       LOG.debug("==> RangerServiceAtlas.validateConfig Service: (" + serviceName + " )");
                  }
                if ( configs != null) {
                       try  {
                                responseMap = AtlasResourceMgr.validateConfig(serviceName, configs);
                        } catch (Exception e) {
                                LOG.error("<== RangerServiceAtlas.validateConfig Error:" + e);
                                throw e;
                        }
                }
                if(LOG.isDebugEnabled()) {
                        LOG.debug("<== RangerServiceAtlas.validateConfig Response : (" + responseMap + " )");
                }
        return responseMap;
    }


    @Override
    public List<String> lookupResource(ResourceLookupContext context) throws Exception {
                List<String> ret = new ArrayList<String>();
                String serviceName = getServiceName();
                Map<String,String> configs = getConfigs();
                if(LOG.isDebugEnabled()) {
                        LOG.debug("==> RangerServiceAtlas.lookupResource Context: (" + context + ")");
                }
                if (context != null) {
                        try {
                               ret  = AtlasResourceMgr.getAtlasTermResources(serviceName,configs,context);
                        } catch (Exception e) {
                                LOG.error( "<==RangerServiceAtlas.lookupResource Error : " + e);
                        throw e;
                        }
                }
                if(LOG.isDebugEnabled()) {
                        LOG.debug("<== RangerServiceAtlas.lookupResource Response: (" + ret + ")");
                }
                return ret;
     }
}
