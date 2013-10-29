/*
 * Copyright 2013 Dominic Masters.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domsplace.DomsCommands.DataManagers;

import com.domsplace.DomsCommands.Bases.DataManager;
import com.domsplace.DomsCommands.Enums.ManagerType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dominic Masters
 */
public class RulesManager extends DataManager {
    public List<String> rules;
    
    public RulesManager() {
        super(ManagerType.RULES);
    }
    
    @Override
    public void tryLoad() throws IOException {
        File file = new File(getDataFolder(), "rules.txt");
        if(!file.exists()) {
            file.createNewFile();
            InputStream rules = getPlugin().getResource("rules.txt");
            OutputStream os = new FileOutputStream(file);
            
            int read;
            while((read = rules.read()) != -1) {
                os.write(read);
            }
            os.close();
            rules.close();
        }
        
        BufferedReader rules = new BufferedReader(new FileReader(file));
        String line;
        
        this.rules = new ArrayList<String>();
        while((line = rules.readLine()) != null) {
            this.rules.add(line);
        }
        rules.close();
    }
}
