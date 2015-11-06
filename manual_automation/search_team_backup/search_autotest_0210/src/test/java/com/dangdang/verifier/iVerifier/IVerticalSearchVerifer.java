package com.dangdang.verifier.iVerifier;

import org.apache.log4j.Logger;

import com.dangdang.data.VerticalSearchQuery;

public interface IVerticalSearchVerifer {

	public static Logger logger = Logger.getLogger(IVerticalSearchVerifer.class);

	boolean verifier(VerticalSearchQuery query);
}
