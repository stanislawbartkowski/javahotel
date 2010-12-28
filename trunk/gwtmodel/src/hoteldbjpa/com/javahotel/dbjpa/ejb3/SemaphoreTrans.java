/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

package com.javahotel.dbjpa.ejb3;

import java.util.concurrent.Semaphore;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class SemaphoreTrans {

	private static final int MAXR = 100;

	private final Semaphore semB;
	private final Semaphore semR;

	SemaphoreTrans() {
		semB = new Semaphore(1, true);
		semR = new Semaphore(MAXR, true);
	}

	void startT(final boolean traB) {
		if (traB) {
			semB.acquireUninterruptibly();
			semR.drainPermits();
		} else {
			semR.acquireUninterruptibly();
		}
	}

	void stopT(final boolean traB) {
		if (traB) {
			semB.release();
			semR.release(MAXR);
		} else {
			semR.release();
		}
	}

}
