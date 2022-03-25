/**
 * Copyright 2016 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jebtk.graphplot;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jebtk.core.collections.ImmutableList;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.Io;
import org.jebtk.core.text.TextUtils;

/**
 * The class Aliases.
 */
public class Aliases extends ImmutableList<Alias> {

	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets the alias.
	 *
	 * @param label the label
	 * @return the alias
	 */
	public String getAlias(String label) {

		String newLabel = new String(label);

		for (Alias alias : this) {
			newLabel = newLabel.replaceAll(alias.from, alias.to);
		}

		return newLabel;
	}

	/**
	 * Parses the file.
	 *
	 * @param file the file
	 * @return the aliases
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Aliases parse(Path file) throws IOException {
		Aliases aliases = new Aliases();

		String line;
		List<String> tokens;

		BufferedReader reader = FileUtils.newBufferedReader(file);

		try {
			while ((line = reader.readLine()) != null) {
				if (Io.isEmptyLine(line)) {
					continue;
				}

				tokens = TextUtils.tabSplit(line);

				// Labels

				String label = tokens.get(0);

				String alias = tokens.get(1);

				aliases.add(new Alias(label, alias));
			}
		} finally {
			reader.close();
		}

		return aliases;
	}
}
