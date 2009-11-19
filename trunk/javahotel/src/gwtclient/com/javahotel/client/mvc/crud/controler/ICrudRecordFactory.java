/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.crud.controler;

import java.util.List;

import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.persistrecord.IPersistRecord;
import com.javahotel.client.mvc.record.extractor.IRecordExtractor;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.common.toobject.AbstractTo;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public interface ICrudRecordFactory {

	RecordModel getNew(AbstractTo beforea, AbstractTo a);

	IRecordValidator getValidator();

	IRecordValidator getValidatorAux();

	ICrudView getView(RecordModel a, ICrudAccept acc, int actionId, IPanel vp);

	IPersistRecord getPersist();

	ICrudRecordControler getControler();

	ICrudReadModel getCrudRead();

	IRecordView getRView(ICrudView v);

	IRecordExtractor getExtractor();

	void show(ICrudView v);

	ICrudPersistSignal getPersistSignal();

	ITableSignalClicked getClicked();

	ICrudChooseTable getChoose();

	List<RecordField> getDef();
}
