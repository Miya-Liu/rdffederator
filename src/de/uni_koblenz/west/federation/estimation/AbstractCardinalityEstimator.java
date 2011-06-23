/*
 * This file is part of RDF Federator.
 * Copyright 2011 Olaf Goerlitz
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
package de.uni_koblenz.west.federation.estimation;

import java.util.HashMap;
import java.util.Map;

import org.openrdf.query.algebra.TupleExpr;
import org.openrdf.query.algebra.UnaryTupleOperator;
import org.openrdf.query.algebra.helpers.QueryModelVisitorBase;

import de.uni_koblenz.west.federation.model.RemoteQuery;

/**
 * @author Olaf Goerlitz
 */
public abstract class AbstractCardinalityEstimator extends QueryModelVisitorBase<RuntimeException> implements ModelEvaluator {

	protected Map<TupleExpr, Double> cardIndex = new HashMap<TupleExpr, Double>();
	
	@Override
	public Double process(TupleExpr expr) {
		synchronized (this) {
//			cardIndex.clear();
			expr.visit(this);
			return cardIndex.get(expr);
		}
	}
	
	@Override
	protected void meetUnaryTupleOperator(UnaryTupleOperator node)
			throws RuntimeException {
		if (node instanceof RemoteQuery) {
			meet((RemoteQuery) node);
		} else {
			super.meetUnaryTupleOperator(node);
		}
	}
	
	protected void meet(RemoteQuery node) {
		if (getIndexCard(node) != null)
			return;
		node.getArg().visit(this);
		setIndexCard(node, getIndexCard(node.getArg()));
	}
	
	protected Double getIndexCard(TupleExpr expr) {
		return this.cardIndex.get(expr);
	}
	
	protected void setIndexCard(TupleExpr expr, Double value) {
		this.cardIndex.put(expr, value);
	}

}
