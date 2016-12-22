package org.migration.fix.impl;

import org.migration.fix.FixHelper;
import org.migration.fix.ObjectExtracted;
import org.migration.tokenizer.ITokenize;

public class ForeignFixTail extends FixHelper {

	@Override
	public void fix(ObjectExtracted o) {
		ITokenize tot = getT(o.getO());
		String w;

		int counter = 0;
		boolean wasReference = false;
		while ((w = tot.readNextWord()) != null) {
			if (w.equalsIgnoreCase("REFERENCES")) {
				wasReference = true;
				continue;
			}
			if (w.equals("("))
				counter++;
			if (w.equals(")"))
				if (--counter == 0 && wasReference) {
					removeRestOfLine(tot);
					String line = getLastLine(tot);
					replaceLastLine(tot, line + " ;");
					break;
				}
		}
		// replace
		replaceLines(o.getO(), tot);		
	}

}
