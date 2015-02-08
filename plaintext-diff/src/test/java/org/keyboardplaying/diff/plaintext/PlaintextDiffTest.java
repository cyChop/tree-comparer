/*
 * Test harness for diff_match_patch.java
 *
 * Copyright 2006 Google Inc.
 * http://code.google.com/p/google-diff-match-patch/
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

package org.keyboardplaying.diff.plaintext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.keyboardplaying.diff.plaintext.PlaintextDiff.LinesToCharsResult;

public class PlaintextDiffTest {

    private PlaintextDiff dmp;
    private Operation DELETE = Operation.DELETE;
    private Operation EQUAL = Operation.EQUAL;
    private Operation INSERT = Operation.INSERT;

    @Before
    public void setUp() {
        // Create an instance of the diff_match_patch object.
        dmp = new PlaintextDiff();
    }

    // DIFF TEST FUNCTIONS
    @Test
    public void testDiffCommonPrefix() {
        // Detect any common prefix.
        assertEquals("diffCommonPrefix: Null case.", 0, dmp.diffCommonPrefix("abc", "xyz"));

        assertEquals("diffCommonPrefix: Non-null case.", 4,
                dmp.diffCommonPrefix("1234abcdef", "1234xyz"));

        assertEquals("diffCommonPrefix: Whole case.", 4, dmp.diffCommonPrefix("1234", "1234xyz"));
    }

    @Test
    public void testDiffCommonSuffix() {
        // Detect any common suffix.
        assertEquals("diffCommonSuffix: Null case.", 0, dmp.diffCommonSuffix("abc", "xyz"));

        assertEquals("diffCommonSuffix: Non-null case.", 4,
                dmp.diffCommonSuffix("abcdef1234", "xyz1234"));

        assertEquals("diffCommonSuffix: Whole case.", 4, dmp.diffCommonSuffix("1234", "xyz1234"));
    }

    @Test
    public void testDiffCommonOverlap() {
        // Detect any suffix/prefix overlap.
        assertEquals("diffCommonOverlap: Null case.", 0, dmp.diffCommonOverlap("", "abcd"));

        assertEquals("diffCommonOverlap: Whole case.", 3, dmp.diffCommonOverlap("abc", "abcd"));

        assertEquals("diffCommonOverlap: No overlap.", 0, dmp.diffCommonOverlap("123456", "abcd"));

        assertEquals("diffCommonOverlap: Overlap.", 3,
                dmp.diffCommonOverlap("123456xxx", "xxxabcd"));

        // Some overly clever languages (C#) may treat ligatures as equal to their
        // component letters. E.g. U+FB01 == 'fi'
        assertEquals("diffCommonOverlap: Unicode.", 0, dmp.diffCommonOverlap("fi", "\ufb01i"));
    }

    @Test
    public void testDiffHalfmatch() {
        // Detect a halfmatch.
        dmp.setTimeout(1);
        assertNull("diffHalfMatch: No match #1.", dmp.diffHalfMatch("1234567890", "abcdef"));

        assertNull("diffHalfMatch: No match #2.", dmp.diffHalfMatch("12345", "23"));

        assertArrayEquals("diffHalfMatch: Single Match #1.", new String[] { "12", "90", "a", "z",
                "345678" }, dmp.diffHalfMatch("1234567890", "a345678z"));

        assertArrayEquals("diffHalfMatch: Single Match #2.", new String[] { "a", "z", "12", "90",
                "345678" }, dmp.diffHalfMatch("a345678z", "1234567890"));

        assertArrayEquals("diffHalfMatch: Single Match #3.", new String[] { "abc", "z", "1234",
                "0", "56789" }, dmp.diffHalfMatch("abc56789z", "1234567890"));

        assertArrayEquals("diffHalfMatch: Single Match #4.", new String[] { "a", "xyz", "1",
                "7890", "23456" }, dmp.diffHalfMatch("a23456xyz", "1234567890"));

        assertArrayEquals("diffHalfMatch: Multiple Matches #1.", new String[] { "12123", "123121",
                "a", "z", "1234123451234" },
                dmp.diffHalfMatch("121231234123451234123121", "a1234123451234z"));

        assertArrayEquals("diffHalfMatch: Multiple Matches #2.", new String[] { "", "-=-=-=-=-=",
                "x", "", "x-=-=-=-=-=-=-=" },
                dmp.diffHalfMatch("x-=-=-=-=-=-=-=-=-=-=-=-=", "xx-=-=-=-=-=-=-="));

        assertArrayEquals("diffHalfMatch: Multiple Matches #3.", new String[] { "-=-=-=-=-=", "",
                "", "y", "-=-=-=-=-=-=-=y" },
                dmp.diffHalfMatch("-=-=-=-=-=-=-=-=-=-=-=-=y", "-=-=-=-=-=-=-=yy"));

        // Optimal diff would be -q+x=H-i+e=lloHe+Hu=llo-Hew+y not -qHillo+x=HelloHe-w+Hulloy
        assertArrayEquals("diffHalfMatch: Non-optimal halfmatch.", new String[] { "qHillo", "w",
                "x", "Hulloy", "HelloHe" }, dmp.diffHalfMatch("qHilloHelloHew", "xHelloHeHulloy"));

        dmp.setTimeout(0);
        assertNull("diffHalfMatch: Optimal no halfmatch.",
                dmp.diffHalfMatch("qHilloHelloHew", "xHelloHeHulloy"));
    }

    @Test
    public void testDiffLinesToChars() {
        // Convert lines down to characters.
        ArrayList<String> tmpVector = new ArrayList<String>();
        tmpVector.add("");
        tmpVector.add("alpha\n");
        tmpVector.add("beta\n");
        assertLinesToCharsResultEquals("diffLinesToChars: Shared lines.", new LinesToCharsResult(
                "\u0001\u0002\u0001", "\u0002\u0001\u0002", tmpVector), dmp.diffLinesToChars(
                "alpha\nbeta\nalpha\n", "beta\nalpha\nbeta\n"));

        tmpVector.clear();
        tmpVector.add("");
        tmpVector.add("alpha\r\n");
        tmpVector.add("beta\r\n");
        tmpVector.add("\r\n");
        assertLinesToCharsResultEquals("diffLinesToChars: Empty string and blank lines.",
                new LinesToCharsResult("", "\u0001\u0002\u0003\u0003", tmpVector),
                dmp.diffLinesToChars("", "alpha\r\nbeta\r\n\r\n\r\n"));

        tmpVector.clear();
        tmpVector.add("");
        tmpVector.add("a");
        tmpVector.add("b");
        assertLinesToCharsResultEquals("diffLinesToChars: No linebreaks.", new LinesToCharsResult(
                "\u0001", "\u0002", tmpVector), dmp.diffLinesToChars("a", "b"));

        // More than 256 to reveal any 8-bit limitations.
        int n = 300;
        tmpVector.clear();
        StringBuilder lineList = new StringBuilder();
        StringBuilder charList = new StringBuilder();
        for (int x = 1; x < n + 1; x++) {
            tmpVector.add(x + "\n");
            lineList.append(x + "\n");
            charList.append(String.valueOf((char) x));
        }
        assertEquals(n, tmpVector.size());
        String lines = lineList.toString();
        String chars = charList.toString();
        assertEquals(n, chars.length());
        tmpVector.add(0, "");
        assertLinesToCharsResultEquals("diffLinesToChars: More than 256.", new LinesToCharsResult(
                chars, "", tmpVector), dmp.diffLinesToChars(lines, ""));
    }

    @Test
    public void testDiffCharsToLines() {
        // First check that Diff equality works.
        assertTrue("diffCharsToLines: Equality #1.",
                new Diff(EQUAL, "a").equals(new Diff(EQUAL, "a")));

        assertEquals("diffCharsToLines: Equality #2.", new Diff(EQUAL, "a"), new Diff(EQUAL, "a"));

        // Convert chars up to lines.
        LinkedList<Diff> diffs = diffList(new Diff(EQUAL, "\u0001\u0002\u0001"), new Diff(INSERT,
                "\u0002\u0001\u0002"));
        ArrayList<String> tmpVector = new ArrayList<String>();
        tmpVector.add("");
        tmpVector.add("alpha\n");
        tmpVector.add("beta\n");
        dmp.diffCharsToLines(diffs, tmpVector);
        assertEquals(
                "diffCharsToLines: Shared lines.",
                diffList(new Diff(EQUAL, "alpha\nbeta\nalpha\n"), new Diff(INSERT,
                        "beta\nalpha\nbeta\n")), diffs);

        // More than 256 to reveal any 8-bit limitations.
        int n = 300;
        tmpVector.clear();
        StringBuilder lineList = new StringBuilder();
        StringBuilder charList = new StringBuilder();
        for (int x = 1; x < n + 1; x++) {
            tmpVector.add(x + "\n");
            lineList.append(x + "\n");
            charList.append(String.valueOf((char) x));
        }
        assertEquals(n, tmpVector.size());
        String lines = lineList.toString();
        String chars = charList.toString();
        assertEquals(n, chars.length());
        tmpVector.add(0, "");
        diffs = diffList(new Diff(DELETE, chars));
        dmp.diffCharsToLines(diffs, tmpVector);
        assertEquals("diffCharsToLines: More than 256.", diffList(new Diff(DELETE, lines)), diffs);
    }

    @Test
    public void testDiffCleanupMerge() {
        // Cleanup a messy diff.
        LinkedList<Diff> diffs = diffList();
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Null case.", diffList(), diffs);

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "b"), new Diff(INSERT, "c"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: No change case.",
                diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "b"), new Diff(INSERT, "c")), diffs);

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(EQUAL, "b"), new Diff(EQUAL, "c"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Merge equalities.", diffList(new Diff(EQUAL, "abc")), diffs);

        diffs = diffList(new Diff(DELETE, "a"), new Diff(DELETE, "b"), new Diff(DELETE, "c"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Merge deletions.", diffList(new Diff(DELETE, "abc")), diffs);

        diffs = diffList(new Diff(INSERT, "a"), new Diff(INSERT, "b"), new Diff(INSERT, "c"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Merge insertions.", diffList(new Diff(INSERT, "abc")),
                diffs);

        diffs = diffList(new Diff(DELETE, "a"), new Diff(INSERT, "b"), new Diff(DELETE, "c"),
                new Diff(INSERT, "d"), new Diff(EQUAL, "e"), new Diff(EQUAL, "f"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Merge interweave.",
                diffList(new Diff(DELETE, "ac"), new Diff(INSERT, "bd"), new Diff(EQUAL, "ef")),
                diffs);

        diffs = diffList(new Diff(DELETE, "a"), new Diff(INSERT, "abc"), new Diff(DELETE, "dc"));
        dmp.diffCleanupMerge(diffs);
        assertEquals(
                "diffCleanupMerge: Prefix and suffix detection.",
                diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "d"), new Diff(INSERT, "b"),
                        new Diff(EQUAL, "c")), diffs);

        diffs = diffList(new Diff(EQUAL, "x"), new Diff(DELETE, "a"), new Diff(INSERT, "abc"),
                new Diff(DELETE, "dc"), new Diff(EQUAL, "y"));
        dmp.diffCleanupMerge(diffs);
        assertEquals(
                "diffCleanupMerge: Prefix and suffix detection with equalities.",
                diffList(new Diff(EQUAL, "xa"), new Diff(DELETE, "d"), new Diff(INSERT, "b"),
                        new Diff(EQUAL, "cy")), diffs);

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(INSERT, "ba"), new Diff(EQUAL, "c"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Slide edit left.",
                diffList(new Diff(INSERT, "ab"), new Diff(EQUAL, "ac")), diffs);

        diffs = diffList(new Diff(EQUAL, "c"), new Diff(INSERT, "ab"), new Diff(EQUAL, "a"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Slide edit right.",
                diffList(new Diff(EQUAL, "ca"), new Diff(INSERT, "ba")), diffs);

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "b"), new Diff(EQUAL, "c"),
                new Diff(DELETE, "ac"), new Diff(EQUAL, "x"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Slide edit left recursive.",
                diffList(new Diff(DELETE, "abc"), new Diff(EQUAL, "acx")), diffs);

        diffs = diffList(new Diff(EQUAL, "x"), new Diff(DELETE, "ca"), new Diff(EQUAL, "c"),
                new Diff(DELETE, "b"), new Diff(EQUAL, "a"));
        dmp.diffCleanupMerge(diffs);
        assertEquals("diffCleanupMerge: Slide edit right recursive.",
                diffList(new Diff(EQUAL, "xca"), new Diff(DELETE, "cba")), diffs);
    }

    @Test
    public void testDiffCleanupSemanticLossless() {
        // Slide diffs to match logical boundaries.
        LinkedList<Diff> diffs = diffList();
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals("diffCleanupSemanticLossless: Null case.", diffList(), diffs);

        diffs = diffList(new Diff(EQUAL, "AAA\r\n\r\nBBB"), new Diff(INSERT, "\r\nDDD\r\n\r\nBBB"),
                new Diff(EQUAL, "\r\nEEE"));
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals(
                "diffCleanupSemanticLossless: Blank lines.",
                diffList(new Diff(EQUAL, "AAA\r\n\r\n"), new Diff(INSERT, "BBB\r\nDDD\r\n\r\n"),
                        new Diff(EQUAL, "BBB\r\nEEE")), diffs);

        diffs = diffList(new Diff(EQUAL, "AAA\r\nBBB"), new Diff(INSERT, " DDD\r\nBBB"), new Diff(
                EQUAL, " EEE"));
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals(
                "diffCleanupSemanticLossless: Line boundaries.",
                diffList(new Diff(EQUAL, "AAA\r\n"), new Diff(INSERT, "BBB DDD\r\n"), new Diff(
                        EQUAL, "BBB EEE")), diffs);

        diffs = diffList(new Diff(EQUAL, "The c"), new Diff(INSERT, "ow and the c"), new Diff(
                EQUAL, "at."));
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals(
                "diffCleanupSemanticLossless: Word boundaries.",
                diffList(new Diff(EQUAL, "The "), new Diff(INSERT, "cow and the "), new Diff(EQUAL,
                        "cat.")), diffs);

        diffs = diffList(new Diff(EQUAL, "The-c"), new Diff(INSERT, "ow-and-the-c"), new Diff(
                EQUAL, "at."));
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals(
                "diffCleanupSemanticLossless: Alphanumeric boundaries.",
                diffList(new Diff(EQUAL, "The-"), new Diff(INSERT, "cow-and-the-"), new Diff(EQUAL,
                        "cat.")), diffs);

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "a"), new Diff(EQUAL, "ax"));
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals("diffCleanupSemanticLossless: Hitting the start.",
                diffList(new Diff(DELETE, "a"), new Diff(EQUAL, "aax")), diffs);

        diffs = diffList(new Diff(EQUAL, "xa"), new Diff(DELETE, "a"), new Diff(EQUAL, "a"));
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals("diffCleanupSemanticLossless: Hitting the end.",
                diffList(new Diff(EQUAL, "xaa"), new Diff(DELETE, "a")), diffs);

        diffs = diffList(new Diff(EQUAL, "The xxx. The "), new Diff(INSERT, "zzz. The "), new Diff(
                EQUAL, "yyy."));
        dmp.diffCleanupSemanticLossless(diffs);
        assertEquals(
                "diffCleanupSemanticLossless: Sentence boundaries.",
                diffList(new Diff(EQUAL, "The xxx."), new Diff(INSERT, " The zzz."), new Diff(
                        EQUAL, " The yyy.")), diffs);
    }

    @Test
    public void testDiffCleanupSemantic() {
        // Cleanup semantically trivial equalities.
        LinkedList<Diff> diffs = diffList();
        dmp.cleanupSemantic(diffs);
        assertEquals("diffCleanupSemantic: Null case.", diffList(), diffs);

        diffs = diffList(new Diff(DELETE, "ab"), new Diff(INSERT, "cd"), new Diff(EQUAL, "12"),
                new Diff(DELETE, "e"));
        dmp.cleanupSemantic(diffs);
        assertEquals(
                "diffCleanupSemantic: No elimination #1.",
                diffList(new Diff(DELETE, "ab"), new Diff(INSERT, "cd"), new Diff(EQUAL, "12"),
                        new Diff(DELETE, "e")), diffs);

        diffs = diffList(new Diff(DELETE, "abc"), new Diff(INSERT, "ABC"), new Diff(EQUAL, "1234"),
                new Diff(DELETE, "wxyz"));
        dmp.cleanupSemantic(diffs);
        assertEquals(
                "diffCleanupSemantic: No elimination #2.",
                diffList(new Diff(DELETE, "abc"), new Diff(INSERT, "ABC"), new Diff(EQUAL, "1234"),
                        new Diff(DELETE, "wxyz")), diffs);

        diffs = diffList(new Diff(DELETE, "a"), new Diff(EQUAL, "b"), new Diff(DELETE, "c"));
        dmp.cleanupSemantic(diffs);
        assertEquals("diffCleanupSemantic: Simple elimination.",
                diffList(new Diff(DELETE, "abc"), new Diff(INSERT, "b")), diffs);

        diffs = diffList(new Diff(DELETE, "ab"), new Diff(EQUAL, "cd"), new Diff(DELETE, "e"),
                new Diff(EQUAL, "f"), new Diff(INSERT, "g"));
        dmp.cleanupSemantic(diffs);
        assertEquals("diffCleanupSemantic: Backpass elimination.",
                diffList(new Diff(DELETE, "abcdef"), new Diff(INSERT, "cdfg")), diffs);

        diffs = diffList(new Diff(INSERT, "1"), new Diff(EQUAL, "A"), new Diff(DELETE, "B"),
                new Diff(INSERT, "2"), new Diff(EQUAL, "_"), new Diff(INSERT, "1"), new Diff(EQUAL,
                        "A"), new Diff(DELETE, "B"), new Diff(INSERT, "2"));
        dmp.cleanupSemantic(diffs);
        assertEquals("diffCleanupSemantic: Multiple elimination.",
                diffList(new Diff(DELETE, "AB_AB"), new Diff(INSERT, "1A2_1A2")), diffs);

        diffs = diffList(new Diff(EQUAL, "The c"), new Diff(DELETE, "ow and the c"), new Diff(
                EQUAL, "at."));
        dmp.cleanupSemantic(diffs);
        assertEquals(
                "diffCleanupSemantic: Word boundaries.",
                diffList(new Diff(EQUAL, "The "), new Diff(DELETE, "cow and the "), new Diff(EQUAL,
                        "cat.")), diffs);

        diffs = diffList(new Diff(DELETE, "abcxx"), new Diff(INSERT, "xxdef"));
        dmp.cleanupSemantic(diffs);
        assertEquals("diffCleanupSemantic: No overlap elimination.",
                diffList(new Diff(DELETE, "abcxx"), new Diff(INSERT, "xxdef")), diffs);

        diffs = diffList(new Diff(DELETE, "abcxxx"), new Diff(INSERT, "xxxdef"));
        dmp.cleanupSemantic(diffs);
        assertEquals("diffCleanupSemantic: Overlap elimination.",
                diffList(new Diff(DELETE, "abc"), new Diff(EQUAL, "xxx"), new Diff(INSERT, "def")),
                diffs);

        diffs = diffList(new Diff(DELETE, "xxxabc"), new Diff(INSERT, "defxxx"));
        dmp.cleanupSemantic(diffs);
        assertEquals("diffCleanupSemantic: Reverse overlap elimination.",
                diffList(new Diff(INSERT, "def"), new Diff(EQUAL, "xxx"), new Diff(DELETE, "abc")),
                diffs);

        diffs = diffList(new Diff(DELETE, "abcd1212"), new Diff(INSERT, "1212efghi"), new Diff(
                EQUAL, "----"), new Diff(DELETE, "A3"), new Diff(INSERT, "3BC"));
        dmp.cleanupSemantic(diffs);
        assertEquals(
                "diffCleanupSemantic: Two overlap eliminations.",
                diffList(new Diff(DELETE, "abcd"), new Diff(EQUAL, "1212"), new Diff(INSERT,
                        "efghi"), new Diff(EQUAL, "----"), new Diff(DELETE, "A"), new Diff(EQUAL,
                        "3"), new Diff(INSERT, "BC")), diffs);
    }

    @Test
    public void testDiffCleanupEfficiency() {
        // Cleanup operationally trivial equalities.
        dmp.setEditCost((short) 4);
        LinkedList<Diff> diffs = diffList();
        dmp.diffCleanupEfficiency(diffs);
        assertEquals("diffCleanupEfficiency: Null case.", diffList(), diffs);

        diffs = diffList(new Diff(DELETE, "ab"), new Diff(INSERT, "12"), new Diff(EQUAL, "wxyz"),
                new Diff(DELETE, "cd"), new Diff(INSERT, "34"));
        dmp.diffCleanupEfficiency(diffs);
        assertEquals(
                "diffCleanupEfficiency: No elimination.",
                diffList(new Diff(DELETE, "ab"), new Diff(INSERT, "12"), new Diff(EQUAL, "wxyz"),
                        new Diff(DELETE, "cd"), new Diff(INSERT, "34")), diffs);

        diffs = diffList(new Diff(DELETE, "ab"), new Diff(INSERT, "12"), new Diff(EQUAL, "xyz"),
                new Diff(DELETE, "cd"), new Diff(INSERT, "34"));
        dmp.diffCleanupEfficiency(diffs);
        assertEquals("diffCleanupEfficiency: Four-edit elimination.",
                diffList(new Diff(DELETE, "abxyzcd"), new Diff(INSERT, "12xyz34")), diffs);

        diffs = diffList(new Diff(INSERT, "12"), new Diff(EQUAL, "x"), new Diff(DELETE, "cd"),
                new Diff(INSERT, "34"));
        dmp.diffCleanupEfficiency(diffs);
        assertEquals("diffCleanupEfficiency: Three-edit elimination.",
                diffList(new Diff(DELETE, "xcd"), new Diff(INSERT, "12x34")), diffs);

        diffs = diffList(new Diff(DELETE, "ab"), new Diff(INSERT, "12"), new Diff(EQUAL, "xy"),
                new Diff(INSERT, "34"), new Diff(EQUAL, "z"), new Diff(DELETE, "cd"), new Diff(
                        INSERT, "56"));
        dmp.diffCleanupEfficiency(diffs);
        assertEquals("diffCleanupEfficiency: Backpass elimination.",
                diffList(new Diff(DELETE, "abxyzcd"), new Diff(INSERT, "12xy34z56")), diffs);

        dmp.setEditCost((short) 5);
        diffs = diffList(new Diff(DELETE, "ab"), new Diff(INSERT, "12"), new Diff(EQUAL, "wxyz"),
                new Diff(DELETE, "cd"), new Diff(INSERT, "34"));
        dmp.diffCleanupEfficiency(diffs);
        assertEquals("diffCleanupEfficiency: High cost elimination.",
                diffList(new Diff(DELETE, "abwxyzcd"), new Diff(INSERT, "12wxyz34")), diffs);
        dmp.setEditCost((short) 4);
    }

    @Test
    public void testDiffPrettyHtml() {
        // Pretty print.
        LinkedList<Diff> diffs = diffList(new Diff(EQUAL, "a\n"), new Diff(DELETE, "<B>b</B>"),
                new Diff(INSERT, "c&d"));
        assertEquals(
                "diffPrettyHtml:",
                "<span>a&para;<br></span><del style=\"background:#ffe6e6;\">&lt;B&gt;b&lt;/B&gt;</del><ins style=\"background:#e6ffe6;\">c&amp;d</ins>",
                dmp.diffPrettyHtml(diffs));
    }

    @Test
    public void testDiffText() {
        // Compute the source and destination texts.
        LinkedList<Diff> diffs = diffList(new Diff(EQUAL, "jump"), new Diff(DELETE, "s"), new Diff(
                INSERT, "ed"), new Diff(EQUAL, " over "), new Diff(DELETE, "the"), new Diff(INSERT,
                "a"), new Diff(EQUAL, " lazy"));
        assertEquals("diffText1:", "jumps over the lazy", dmp.diffText1(diffs));
        assertEquals("diffText2:", "jumped over a lazy", dmp.diffText2(diffs));
    }

    @Test
    public void testDiffDelta() {
        // Convert a diff into delta string.
        LinkedList<Diff> diffs = diffList(new Diff(EQUAL, "jump"), new Diff(DELETE, "s"), new Diff(
                INSERT, "ed"), new Diff(EQUAL, " over "), new Diff(DELETE, "the"), new Diff(INSERT,
                "a"), new Diff(EQUAL, " lazy"), new Diff(INSERT, "old dog"));
        String text1 = dmp.diffText1(diffs);
        assertEquals("diffText1: Base text.", "jumps over the lazy", text1);

        String delta = dmp.diffToDelta(diffs);
        assertEquals("diffToDelta:", "=4\t-1\t+ed\t=6\t-3\t+a\t=5\t+old dog", delta);

        // Convert delta string into a diff.
        assertEquals("diffFromDelta: Normal.", diffs, dmp.diffFromDelta(text1, delta));

        // Generates error (19 < 20).
        try {
            dmp.diffFromDelta(text1 + "x", delta);
            fail("diffFromDelta: Too long.");
        } catch (IllegalArgumentException ex) {
            // Exception expected.
        }

        // Generates error (19 > 18).
        try {
            dmp.diffFromDelta(text1.substring(1), delta);
            fail("diffFromDelta: Too short.");
        } catch (IllegalArgumentException ex) {
            // Exception expected.
        }

        // Generates error (%c3%xy invalid Unicode).
        try {
            dmp.diffFromDelta("", "+%c3%xy");
            fail("diffFromDelta: Invalid character.");
        } catch (IllegalArgumentException ex) {
            // Exception expected.
        }

        // Test deltas with special characters.
        diffs = diffList(new Diff(EQUAL, "\u0680 \000 \t %"), new Diff(DELETE, "\u0681 \001 \n ^"),
                new Diff(INSERT, "\u0682 \002 \\ |"));
        text1 = dmp.diffText1(diffs);
        assertEquals("diffText1: Unicode text.", "\u0680 \000 \t %\u0681 \001 \n ^", text1);

        delta = dmp.diffToDelta(diffs);
        assertEquals("diffToDelta: Unicode.", "=7\t-7\t+%DA%82 %02 %5C %7C", delta);

        assertEquals("diffFromDelta: Unicode.", diffs, dmp.diffFromDelta(text1, delta));

        // Verify pool of unchanged characters.
        diffs = diffList(new Diff(INSERT, "A-Z a-z 0-9 - _ . ! ~ * ' ( ) ; / ? : @ & = + $ , # "));
        String text2 = dmp.diffText2(diffs);
        assertEquals("diffText2: Unchanged characters.",
                "A-Z a-z 0-9 - _ . ! ~ * \' ( ) ; / ? : @ & = + $ , # ", text2);

        delta = dmp.diffToDelta(diffs);
        assertEquals("diffToDelta: Unchanged characters.",
                "+A-Z a-z 0-9 - _ . ! ~ * \' ( ) ; / ? : @ & = + $ , # ", delta);

        // Convert delta string into a diff.
        assertEquals("diffFromDelta: Unchanged characters.", diffs, dmp.diffFromDelta("", delta));
    }

    @Test
    public void testDiffXIndex() {
        // Translate a location in text1 to text2.
        LinkedList<Diff> diffs = diffList(new Diff(DELETE, "a"), new Diff(INSERT, "1234"),
                new Diff(EQUAL, "xyz"));
        assertEquals("diff_xIndex: Translation on equality.", 5, dmp.diffXIndex(diffs, 2));

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "1234"), new Diff(EQUAL, "xyz"));
        assertEquals("diff_xIndex: Translation on deletion.", 1, dmp.diffXIndex(diffs, 3));
    }

    @Test
    public void testDiffLevenshtein() {
        LinkedList<Diff> diffs = diffList(new Diff(DELETE, "abc"), new Diff(INSERT, "1234"),
                new Diff(EQUAL, "xyz"));
        assertEquals("Levenshtein with trailing equality.", 4, dmp.diffLevenshtein(diffs));

        diffs = diffList(new Diff(EQUAL, "xyz"), new Diff(DELETE, "abc"), new Diff(INSERT, "1234"));
        assertEquals("Levenshtein with leading equality.", 4, dmp.diffLevenshtein(diffs));

        diffs = diffList(new Diff(DELETE, "abc"), new Diff(EQUAL, "xyz"), new Diff(INSERT, "1234"));
        assertEquals("Levenshtein with middle equality.", 7, dmp.diffLevenshtein(diffs));
    }

    @Test
    public void testDiffBisect() {
        // Normal.
        String a = "cat";
        String b = "map";
        // Since the resulting diff hasn't been normalized, it would be ok if
        // the insertion and deletion pairs are swapped.
        // If the order changes, tweak this test as required.
        LinkedList<Diff> diffs = diffList(new Diff(DELETE, "c"), new Diff(INSERT, "m"), new Diff(
                EQUAL, "a"), new Diff(DELETE, "t"), new Diff(INSERT, "p"));
        assertEquals("diffBisect: Normal.", diffs, dmp.diffBisect(a, b, Long.MAX_VALUE));

        // Timeout.
        diffs = diffList(new Diff(DELETE, "cat"), new Diff(INSERT, "map"));
        assertEquals("diffBisect: Timeout.", diffs, dmp.diffBisect(a, b, 0));
    }

    @Test
    public void testDiffMain() {
        // Perform a trivial diff.
        LinkedList<Diff> diffs = diffList();
        assertEquals("diff: Null case.", diffs, dmp.diff("", "", false));

        diffs = diffList(new Diff(EQUAL, "abc"));
        assertEquals("diff: Equality.", diffs, dmp.diff("abc", "abc", false));

        diffs = diffList(new Diff(EQUAL, "ab"), new Diff(INSERT, "123"), new Diff(EQUAL, "c"));
        assertEquals("diff: Simple insertion.", diffs, dmp.diff("abc", "ab123c", false));

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "123"), new Diff(EQUAL, "bc"));
        assertEquals("diff: Simple deletion.", diffs, dmp.diff("a123bc", "abc", false));

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(INSERT, "123"), new Diff(EQUAL, "b"),
                new Diff(INSERT, "456"), new Diff(EQUAL, "c"));
        assertEquals("diff: Two insertions.", diffs, dmp.diff("abc", "a123b456c", false));

        diffs = diffList(new Diff(EQUAL, "a"), new Diff(DELETE, "123"), new Diff(EQUAL, "b"),
                new Diff(DELETE, "456"), new Diff(EQUAL, "c"));
        assertEquals("diff: Two deletions.", diffs, dmp.diff("a123b456c", "abc", false));

        // Perform a real diff.
        // Switch off the timeout.
        dmp.setTimeout(0);
        diffs = diffList(new Diff(DELETE, "a"), new Diff(INSERT, "b"));
        assertEquals("diff: Simple case #1.", diffs, dmp.diff("a", "b", false));

        diffs = diffList(new Diff(DELETE, "Apple"), new Diff(INSERT, "Banana"), new Diff(EQUAL,
                "s are a"), new Diff(INSERT, "lso"), new Diff(EQUAL, " fruit."));
        assertEquals("diff: Simple case #2.", diffs,
                dmp.diff("Apples are a fruit.", "Bananas are also fruit.", false));

        diffs = diffList(new Diff(DELETE, "a"), new Diff(INSERT, "\u0680"), new Diff(EQUAL, "x"),
                new Diff(DELETE, "\t"), new Diff(INSERT, "\000"));
        assertEquals("diff: Simple case #3.", diffs, dmp.diff("ax\t", "\u0680x\000", false));

        diffs = diffList(new Diff(DELETE, "1"), new Diff(EQUAL, "a"), new Diff(DELETE, "y"),
                new Diff(EQUAL, "b"), new Diff(DELETE, "2"), new Diff(INSERT, "xab"));
        assertEquals("diff: Overlap #1.", diffs, dmp.diff("1ayb2", "abxab", false));

        diffs = diffList(new Diff(INSERT, "xaxcx"), new Diff(EQUAL, "abc"), new Diff(DELETE, "y"));
        assertEquals("diff: Overlap #2.", diffs, dmp.diff("abcy", "xaxcxabc", false));

        diffs = diffList(new Diff(DELETE, "ABCD"), new Diff(EQUAL, "a"), new Diff(DELETE, "="),
                new Diff(INSERT, "-"), new Diff(EQUAL, "bcd"), new Diff(DELETE, "="), new Diff(
                        INSERT, "-"), new Diff(EQUAL, "efghijklmnopqrs"), new Diff(DELETE,
                        "EFGHIJKLMNOefg"));
        assertEquals("diff: Overlap #3.", diffs,
                dmp.diff("ABCDa=bcd=efghijklmnopqrsEFGHIJKLMNOefg", "a-bcd-efghijklmnopqrs", false));

        diffs = diffList(new Diff(INSERT, " "), new Diff(EQUAL, "a"), new Diff(INSERT, "nd"),
                new Diff(EQUAL, " [[Pennsylvania]]"), new Diff(DELETE, " and [[New"));
        assertEquals("diff: Large equality.", diffs,
                dmp.diff("a [[Pennsylvania]] and [[New", " and [[Pennsylvania]]", false));

        dmp.setTimeout(0.1F); // 100 ms
        String a = "`Twas brillig, and the slithy toves\nDid gyre and gimble in the wabe:\nAll mimsy were the borogoves,\nAnd the mome raths outgrabe.\n";
        String b = "I am the very model of a modern major general,\nI've information vegetable, animal, and mineral,\nI know the kings of England, and I quote the fights historical,\nFrom Marathon to Waterloo, in order categorical.\n";
        // Increase the text lengths by 1024 times to ensure a timeout.
        for (int x = 0; x < 10; x++) {
            a = a + a;
            b = b + b;
        }
        long startTime = System.currentTimeMillis();
        dmp.diff(a, b);
        long endTime = System.currentTimeMillis();
        // Test that we took at least the timeout period.
        assertTrue("diff: Timeout min.", dmp.getTimeout() * 1000 <= endTime - startTime);
        // Test that we didn't take forever (be forgiving).
        // Theoretically this test could fail very occasionally if the
        // OS task swaps or locks up for a second at the wrong moment.
        assertTrue("diff: Timeout max.", dmp.getTimeout() * 1000 * 2 > endTime - startTime);
        dmp.setTimeout(0);

        // Test the linemode speedup.
        // Must be long to pass the 100 char cutoff.
        a = "1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n";
        b = "abcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\nabcdefghij\n";
        assertEquals("diff: Simple line-mode.", dmp.diff(a, b, true), dmp.diff(a, b, false));

        a = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
        b = "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij";
        assertEquals("diff: Single line-mode.", dmp.diff(a, b, true), dmp.diff(a, b, false));

        a = "1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n1234567890\n";
        b = "abcdefghij\n1234567890\n1234567890\n1234567890\nabcdefghij\n1234567890\n1234567890\n1234567890\nabcdefghij\n1234567890\n1234567890\n1234567890\nabcdefghij\n";
        String[] texts_linemode = diffRebuildTexts(dmp.diff(a, b, true));
        String[] texts_textmode = diffRebuildTexts(dmp.diff(a, b, false));
        assertArrayEquals("diff: Overlap line-mode.", texts_textmode, texts_linemode);

        // Test null inputs.
        try {
            dmp.diff(null, null);
            fail("diff: Null inputs.");
        } catch (IllegalArgumentException ex) {
            // Error expected.
        }
    }

    private void assertArrayEquals(String error_msg, Object[] a, Object[] b) {
        List<Object> list_a = Arrays.asList(a);
        List<Object> list_b = Arrays.asList(b);
        assertEquals(error_msg, list_a, list_b);
    }

    private void assertLinesToCharsResultEquals(String error_msg, LinesToCharsResult a,
            LinesToCharsResult b) {
        assertEquals(error_msg, a.chars1, b.chars1);
        assertEquals(error_msg, a.chars2, b.chars2);
        assertEquals(error_msg, a.lineArray, b.lineArray);
    }

    // Construct the two texts which made up the diff originally.
    private static String[] diffRebuildTexts(LinkedList<Diff> diffs) {
        String[] text = { "", "" };
        for (Diff myDiff : diffs) {
            if (myDiff.operation != Operation.INSERT) {
                text[0] += myDiff.text;
            }
            if (myDiff.operation != Operation.DELETE) {
                text[1] += myDiff.text;
            }
        }
        return text;
    }

    // Private function for quickly building lists of diffs.
    private static LinkedList<Diff> diffList(Diff... diffs) {
        LinkedList<Diff> myDiffList = new LinkedList<Diff>();
        for (Diff myDiff : diffs) {
            myDiffList.add(myDiff);
        }
        return myDiffList;
    }
}
