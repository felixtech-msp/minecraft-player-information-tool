package io.felixtech.mcpit.util;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Allows only a defined maximum length.
 */
public final class MaxSizeDocument extends PlainDocument {
    private final int maxSize;

    /**
     * Constructs a new {@code MaxSizeDocument} with the specified maximum length.
     * @param maxSize the maximum size that should be allowed
     * @throws IllegalArgumentException if the specified maximum size if not greater than zero
     */
    public MaxSizeDocument(final int maxSize) {
        if(maxSize <= 0) throw new IllegalArgumentException("Maximum size must be greater than zero!");
        this.maxSize = maxSize;
    }

    /**
     * Inserts some content into the document.
     * <br>If no more content can be added (because of the maximum length) the computer will make a pling-sound.<br>
     * Please see {@link javax.swing.text.PlainDocument} for more information.
     * @param offset the starting offset &gt;= 0
     * @param text the string to insert; does nothing with null/empty strings
     * @param attributeSet the attributes for the inserted content
     * @throws BadLocationException if the given insert position is not a valid position within the document
     */
    @Override
    public void insertString(final int offset, final String text, final AttributeSet attributeSet) throws BadLocationException {
        if(this.isNewLengthOk(text)) {
            super.insertString(offset,text,attributeSet);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean isNewLengthOk(final String text) {
        return this.getLength() + text.length() <= this.maxSize;
    }
}
