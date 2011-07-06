/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResourceInfo implements Serializable {

    private String javaS = null;
    private String cssS = null;
    private boolean customRow = false;
    private String jsAddRowFunc = null;
    private String birtURL = null;
    private String employeeReport = null;
    private String tableWidth = null;
    
    
    public String getTableWidth() {
		return tableWidth;
	}

	public void setTableWidth(String tableWidth) {
		this.tableWidth = tableWidth;
	}

	public String getBirtURL() {
        return birtURL;
    }

    public String getEmployeeReport() {
        return employeeReport;
    }

    public void setBirtURL(String birtURL) {
        this.birtURL = birtURL;
    }

    public void setEmployeeReport(String employeeReport) {
        this.employeeReport = employeeReport;
    }

    public String getJsAddRowFunc() {
        return jsAddRowFunc;
    }

    public void setJsAddRowFunc(String jsAddRowFunc) {
        this.jsAddRowFunc = jsAddRowFunc;
    }

    public String getJavaS() {
        return javaS;
    }

    public void setJavaS(String javaS) {
        this.javaS = javaS;
    }

    public String getCssS() {
        return cssS;
    }

    public void setCssS(String cssS) {
        this.cssS = cssS;
    }

    public boolean isCustomRow() {
        return customRow;
    }

    public void setCustomRow(boolean customRow) {
        this.customRow = customRow;
    }

}
