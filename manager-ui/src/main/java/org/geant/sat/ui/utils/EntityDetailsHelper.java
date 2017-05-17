/*
 * GÉANT BSD Software License
 *
 * Copyright (c) 2017 - 2020, GÉANT
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the GÉANT nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * Disclaimer:
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.geant.sat.ui.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.geant.sat.api.dto.EntityDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.Query;
import com.vaadin.shared.Registration;
import com.vaadin.ui.ItemCaptionGenerator;

/** class implementing helpers for assessor details. */
@SuppressWarnings("serial")
public class EntityDetailsHelper implements ItemCaptionGenerator<EntityDetails>, DataProvider<EntityDetails,String> {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(EntityDetailsHelper.class);
    
    private List<EntityDetails> details;
    
    /**
     * Constructor.
     */
    public EntityDetailsHelper(){
        
    }
    
    /**
     * Constructor for dataprovider case.
     * @param dtls entity details.
     */
    public EntityDetailsHelper(List<EntityDetails> dtls){
        details=dtls;
    }
    
    @Override
    public String apply(EntityDetails item) {
        return display(item);
    }

    /**
     * Converts entity to a human readable string.
     * 
     * @param item
     *            entity details.
     * @return entity display string.
     */
    public static String display(EntityDetails item) {
        if (item.getId() != null) {
            return item.getId() + ": " + item.getName();
        }
        return item.getName();
    }

    /**
     * Creates a new set of entity details by picking entities from "pool" based
     * on "selection". The entities are picked by matching id.
     * 
     * @param pool
     *            to pick entities from
     * @param selection
     *            entities to be picked.
     * @return entities
     */
    public static Set<EntityDetails> selectionToSet(List<EntityDetails> pool, List<EntityDetails> selection) {
        Set<EntityDetails> result = new HashSet<EntityDetails>();
        for (EntityDetails entityDetail : pool) {
            for (EntityDetails preselectedEntityDetail : selection) {
                if (preselectedEntityDetail.getId().equals(entityDetail.getId())) {
                    result.add(entityDetail);
                }
            }
        }
        return result;

    }

    /**
     * 
     * experimental code ->
     * not used.
     */
    
    @Override
    public boolean isInMemory() {
        return true;
    }

    @Override
    public int size(Query<EntityDetails, String> query) {
        return details.size();
    }

    @Override
    public Stream<EntityDetails> fetch(Query<EntityDetails, String> query) {
        LOG.debug("query "+query.getOffset()+"/"+query.getLimit());
        return details.subList(query.getOffset(),query.getOffset()+(query.getLimit()-1)).stream();
    }

    @Override
    public void refreshItem(EntityDetails item) {
        LOG.warn("refreshItem not implemented");
    }

    @Override
    public void refreshAll() {
        LOG.warn("refreshAll not implemented");
    }

    @Override
    public Registration addDataProviderListener(DataProviderListener<EntityDetails> listener) {
        LOG.warn("addDataProviderListener not implemented");
        return null;
    }

}
