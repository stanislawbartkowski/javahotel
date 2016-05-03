/*
 * Copyright 2016 stanislawbartkowski@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gwtmodel.table.factories;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public interface IWebPanelResources {

	String TITLE = "Title";
	String PRODUCTNAME = "ProductName";
	String OWNERNAME = "OwnerName";
	String IMAGELOGOUT = "LogOut";
	String IIMAGEPRODUCT = "ImageProduct";
	String VERSION = "Version";
	String JUIVERSION = "JUIVersion";
	String LOGOUTQUESTION = "LogoutQuestion";
	String STATUSHTML = "StatusHtml";
	String SCROLLWITHDATE = "ScrollWithDate";
	String SCROLLWITHOUTDATE = "ScrollWithoutDate";
	String PROGRESSICON = "PROGRESSICON";
	String SCROLLLEFTEND = "scrollleftend";
	String SCROLLRIGHTEND = "scrollrightend";
	String SCROLLLEFT = "scrollleft";
	String SCROLLRIGHT = "scrollright";
	String CALENDAR = "calendar";
	String ADDROW = "addrow";
	String ADDBEFOREROW = "addbeforerow";
	String CHANGEROW = "changerow";
	String DELETEROW = "deleterow";
	String PANELMENU = "panelmenu";

	String getRes(String res);

}
