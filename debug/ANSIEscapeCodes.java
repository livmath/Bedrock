/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kraken.bedrock.debug;

/**
 *
 * @author adamn
 */
public interface ANSIEscapeCodes{

	static final String CLEAR = "\033[2J";
	static final String CLEAR_FROM_CURSOR = "\033[0J";
	static final String CLEAR_BEFORE_CURSOR = "\033[1J";
	static final String CLEAR_LINE = "\033[2K";
	static final String CLEAR_LINE_FROM_CURSOR = "\033[0K";
	static final String CLEAR_LINE_BEFORE_CURSOR = "\033[1K";

	static final String RESET_STYLES = "\033[0m";
};