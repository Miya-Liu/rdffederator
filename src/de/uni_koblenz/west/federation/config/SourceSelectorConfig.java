/*
 * This file is part of RDF Federator.
 * Copyright 2010 Olaf Goerlitz
 * 
 * RDF Federator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * RDF Federator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with RDF Federator.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * RDF Federator uses libraries from the OpenRDF Sesame Project licensed 
 * under the Aduna BSD-style license. 
 */
package de.uni_koblenz.west.federation.config;

import static de.uni_koblenz.west.federation.config.FederationSailSchema.ATTACH_SAME_AS;
import static de.uni_koblenz.west.federation.config.FederationSailSchema.SLCT_TYPE;
import static de.uni_koblenz.west.federation.config.FederationSailSchema.USE_TYPE_STATS;

import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.sail.config.SailConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Setting details for the sources selector configuration.
 * 
 * @author Olaf Goerlitz
 */
public class SourceSelectorConfig extends AbstractSailConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SourceSelectorConfig.class);
	
	private boolean useTypeStats;
	private boolean attachSameAs;
	
	protected SourceSelectorConfig() {
		super(SLCT_TYPE);
	}
	
	public static SourceSelectorConfig create(Graph model, Resource implNode) throws SailConfigException {
		SourceSelectorConfig config = new SourceSelectorConfig();
		config.parse(model, implNode);
		return config;
	}
	
	public boolean isUseTypeStats() {
		return this.useTypeStats;
	}
	
	public boolean isAttachSameAs() {
		return this.attachSameAs;
	}
	
	@Override
	public Resource export(Graph model) {
		ValueFactory vf = ValueFactoryImpl.getInstance();
		
		Resource self = super.export(model);
		
		model.add(self, USE_TYPE_STATS, vf.createLiteral(this.useTypeStats));
		model.add(self, ATTACH_SAME_AS, vf.createLiteral(this.attachSameAs));
		
		return self;
	}

	@Override
	public void parse(Graph model, Resource implNode) throws SailConfigException {
		super.parse(model, implNode);
		
		try {
			useTypeStats = getObjectLiteral(model, implNode, USE_TYPE_STATS).booleanValue();
		} catch (NullPointerException e) {
			LOGGER.warn("option 'sourceSelection.useTypeStats' not set: default is TRUE");
			useTypeStats = true;
		} catch (IllegalArgumentException e) {
			throw new SailConfigException("not a boolean value in 'sourceSelection.useTypeStats'");
		}
		
		try {
			attachSameAs = getObjectLiteral(model, implNode, ATTACH_SAME_AS).booleanValue();
		} catch (NullPointerException e) {
			LOGGER.warn("option 'sourceSelection.attachSameAs' not set: default is FALSE");
			attachSameAs = false;
		} catch (IllegalArgumentException e) {
			throw new SailConfigException("not a boolean value in 'sourceSelection.attachSameAs'");
		}
		
	}

}