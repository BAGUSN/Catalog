/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.web.ws.views.transformer;

import net.sf.json.JsonConfig;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
public interface JsonOutputTransformer extends OutputTransformer {

    public void setJsonConfig(final JsonConfig jsonConfig);

}
