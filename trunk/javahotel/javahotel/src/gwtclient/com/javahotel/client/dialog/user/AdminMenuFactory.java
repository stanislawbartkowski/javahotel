/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog.user;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IParamKey;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.user.downpayment.DownPaymentControler;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.controller.onerecord.RecordFa;
import com.javahotel.client.mvc.controller.onerecord.RecordFaParam;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.edittable.controller.ControlerEditTableFactory;
import com.javahotel.client.mvc.edittable.controller.IControlerEditTable;
import com.javahotel.client.stackmenu.model.IStackButtonClick;
import com.javahotel.client.stackmenu.model.StackButtonElem;
import com.javahotel.client.stackmenu.model.StackButtonHeader;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.ResObjectP;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class AdminMenuFactory {

    private AdminMenuFactory() {
    }

    static private StackButtonElem getPokoje(final IResLocator rI) {

        IStackButtonClick iClick = new IStackButtonClick() {

            private PokojePanel pa;

            public IMvcWidget getMWidget() {
                return new DefaultMvcWidget(pa.getMWidget().getWidget());
            }

			public void beforeDrawAction() {
                pa = new PokojePanel(rI);				
			}

			public void drawAction() {				
			}
        };

        return new StackButtonElem("Pokoje", iClick);
    }

    static private StackButtonElem getShowBook(final IResLocator rI) {

        IStackButtonClick iClick = new IStackButtonClick() {

            private BookingPanel pa;

            public void beforeDrawAction() {
                pa = new BookingPanel(rI);
            }

            public IMvcWidget getMWidget() {
                return new DefaultMvcWidget(pa);
            }

			public void drawAction() {		
				pa.draw();
			}
        };

        return new StackButtonElem("Pokaż rezerwacje", iClick);
    }

    static private StackButtonElem getBookPanel(final IResLocator rI) {

        IStackButtonClick iClick = new IStackButtonClick() {

            private ICrudControler cPan;

            public void beforeDrawAction() {
                cPan = DictCrudControlerFactory.getCrud(rI,
                        new DictData(DictType.BookingList));
            }

            public IMvcWidget getMWidget() {
                return cPan.getMWidget();
            }

			public void drawAction() {
                cPan.drawTable();				
			}
        };

        return new StackButtonElem("Rezerwuj", iClick);
    }

    static private StackButtonElem getPrePaidPanel(final IResLocator rI) {

        IStackButtonClick iClick = new IStackButtonClick() {

            private DownPaymentControler pa;

            public void beforeDrawAction() {
                pa = new DownPaymentControler(rI);
            }

            public IMvcWidget getMWidget() {
                return pa.getMWidget();
            }

			public void drawAction() {
                pa.show();				
			}
        };
        return new StackButtonElem("Zaliczki", iClick);
    }

    static private StackButtonElem getTest(final IResLocator rI) {

        IStackButtonClick iClick = new IStackButtonClick() {

            private RecordFa fa;

            public void beforeDrawAction() {
                RecordFaParam pa = new RecordFaParam();
                pa.setNewchoosetag(true);
                fa = new RecordFa(rI,
                        new DictData(DictType.CustomerList), pa);
                fa.setModifWidgetStatus(true);
                fa.setNewWidgetStatus(true);
            }

            public IMvcWidget getMWidget() {
                return fa.getMWidget();
            }

			public void drawAction() {
			}
        };
        return new StackButtonElem("Test", iClick);
    }

    static private StackButtonElem getReports(final IResLocator rI) {

        IStackButtonClick iClick = new IStackButtonClick() {

            private final VerticalPanel vp = new VerticalPanel();

            public void beforeDrawAction() {
                Button bu = new Button("Raport");
                ClickListener cl = new ClickListener() {

                    public void onClick(Widget sender) {
                        String urlPref = rI.getParam(IParamKey.REPORTURL);
//                        String url = "http://localhost:8143/birt/frameset?__report=test.rptdesign&sample=my+parameter";
                        String rep = "/home/hotel/workspace/hotelbirt/hotelrep1/report1.rptdesign";
//                        String url = urlPref +
//                                "/frameset?__report=test.rptdesign&sample=my+parameter";
                          String url = urlPref +
                                "/frameset?__report=" + rep;
                      Window.open(url, "", "");
                    }
                };
                bu.addClickListener(cl);
                vp.add(bu);
            }

			public void drawAction() {
			}

			public IMvcWidget getMWidget() {
				// TODO Auto-generated method stub
				return null;
			}
        };
        return new StackButtonElem("Raporty", iClick);
    }

    static private StackButtonElem getTest1(final IResLocator rI) {

        IStackButtonClick iClick = new IStackButtonClick() {

            private IControlerEditTable cPan;

            public void beforeDrawAction() {
                cPan = ControlerEditTableFactory.getTable(rI,
                        new DictData(SpecE.ResGuestList), null);
                ArrayList<ResRoomGuest> gList = new ArrayList<ResRoomGuest>();
                for (int i = 0; i < 3; i++) {
                    ResObjectP re = new ResObjectP();
                    re.setName("No" + i);
                    re.setDescription("Desc" + i);
                    gList.add(new ResRoomGuest(re));
                }
                cPan.getView().getModel().setList(gList);
                cPan.show();
            }

            public IMvcWidget getMWidget() {
                return cPan.getMWidget();
            }


			public void drawAction() {
                cPan.show();				
			}
        };
        return new StackButtonElem("Test1", iClick);
    }

    static ArrayList<StackButtonHeader> getAList(IResLocator rI) {

        ArrayList<StackButtonHeader> hList = new ArrayList<StackButtonHeader>();
        ArrayList<StackButtonElem> aList = new ArrayList<StackButtonElem>();
        aList.add(getPokoje(rI));
        hList.add(new StackButtonHeader("Admin", "people.gif", aList));
                
        aList = new ArrayList<StackButtonElem>();
        aList.add(getShowBook(rI));
        aList.add(getBookPanel(rI));
        aList.add(getPrePaidPanel(rI));
        hList.add(new StackButtonHeader("Rezerwacja", "reports.gif", aList));
        
//        aList = new ArrayList<StackButtonElem>();
//        aList.add(getTest(rI));
//        aList.add(getTest1(rI));
//        aList.add(getReports(rI));
//        hList.add(new StackButtonHeader("Test", "sent.gif", aList));
        return hList;
    }
}
