/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author hotel
 * 
 */
public class IVFieldSetFactory {

    /**
     * Implementation of Set<IVField> Cannot use HashSet<IVField> directly.
     * IVField is an interface and valid implementing of hashCode and equals is
     * not required. Implementation by Collection and using 'eq' method
     * 
     * @author hotel
     * 
     */

    private static class FieldSet implements Set<IVField> {

        /**
         * Wrapper around IVField interface. The only purpose is to implement
         * 'equals' Object method
         * 
         * @author hotel
         * 
         */
        private class IVFieldWrapper {

            private final IVField o;

            IVFieldWrapper(IVField o) {
                this.o = o;
            }

            @Override
            public boolean equals(Object o) {
                IVFieldWrapper v = (IVFieldWrapper) o;
                return this.o.eq(v.o);
            }
        }

        private Collection<IVFieldWrapper> list = new ArrayList<IVFieldWrapper>();

        private class FieldSetIterator implements Iterator<IVField> {

            private final Iterator<IVFieldWrapper> i;

            FieldSetIterator() {
                i = list.iterator();
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Iterator#hasNext()
             */
            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Iterator#next()
             */
            @Override
            public IVField next() {
                IVFieldWrapper w = i.next();
                if (w == null) {
                    return null;
                }
                return w.o;
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Iterator#remove()
             */
            @Override
            public void remove() {
                i.remove();
            }

        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#size()
         */
        @Override
        public int size() {
            return list.size();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#isEmpty()
         */
        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#contains(java.lang.Object)
         */
        @Override
        public boolean contains(Object o) {
            IVField v = (IVField) o;
            return list.contains(new IVFieldWrapper(v));
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#iterator()
         */
        @Override
        public Iterator<IVField> iterator() {
            return new FieldSetIterator();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#toArray()
         */
        @Override
        public Object[] toArray() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#toArray(T[])
         */
        @Override
        public <T> T[] toArray(T[] a) {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#add(java.lang.Object)
         */
        @Override
        public boolean add(IVField e) {
            IVFieldWrapper w = new IVFieldWrapper(e);
            if (list.contains(w)) {
                return false;
            }
            list.add(w);
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#remove(java.lang.Object)
         */
        @Override
        public boolean remove(Object o) {
            IVFieldWrapper w = new IVFieldWrapper((IVField) o);
            if (list.contains(w)) {
                return false;
            }
            list.remove(w);
            return true;
        }

        /**
         * Create IVFieldWrapper collection from IVFIeld collection
         * 
         * @param c
         *            Source IVField collection
         * @param removeExisting
         *            if true then remove IVField already existing in list
         * @return IVFieldWrapper collection
         */
        private Collection<IVFieldWrapper> construct(Collection<?> c,
                boolean removeExisting) {
            Collection<IVFieldWrapper> li = new ArrayList<IVFieldWrapper>();
            for (Object v : c) {
                IVFieldWrapper w = new IVFieldWrapper((IVField) v);
                if (removeExisting) {
                    if (contains(v)) {
                        continue;
                    }
                }
                li.add(new IVFieldWrapper((IVField) v));
            }
            return li;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#containsAll(java.util.Collection)
         */
        @Override
        public boolean containsAll(Collection<?> c) {
            return list.containsAll(construct(c, false));
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#addAll(java.util.Collection)
         */
        @Override
        public boolean addAll(Collection<? extends IVField> c) {
            return list.addAll(construct(c, true));
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#retainAll(java.util.Collection)
         */
        @Override
        public boolean retainAll(Collection<?> c) {
            return list.retainAll(construct(c, false));
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#removeAll(java.util.Collection)
         */
        @Override
        public boolean removeAll(Collection<?> c) {
            return list.removeAll(construct(c, false));
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Set#clear()
         */
        @Override
        public void clear() {
            list.clear();
        }

    }

    private IVFieldSetFactory() {

    }

    /**
     * Factory method : produce Set<IVField> implementation
     * 
     * @return
     */
    public static Set<IVField> construct() {
        return new FieldSet();
    }

}
