/*
 * LineDetail.java
 * 
 * This file is part of CajuScript.
 * 
 * CajuScript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3, or (at your option) 
 * any later version.
 * 
 * CajuScript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with CajuScript.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cajuscript.parser;

/**
 * Line detail.
 * @author eduveks
 */
public class LineDetail implements java.io.Serializable {
    private String content = "";
    private int number = 0;
    
    /**
     * Create new line detail.
     * @param n Line number
     * @param c Line content
     */
    public LineDetail(int n, String c) {
        content = c;
        number = n;
    }
    
    /**
     * Get line content
     * @return Content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Get line number
     * @return Number
     */
    public int getNumber() {
        return number;
    }
    
    /**
     * Set line content
     * @param content Content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Append line content
     * @param content Content
     */
    public void appendContent(String content) {
        this.content += content;
    }
    
    /**
     * Set line number
     * @param number Number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Set line detail.
     * @param n Line number
     * @param c Line content
     */
    public void set(int n, String c) {
        content = c;
        number = n;
    }
}
