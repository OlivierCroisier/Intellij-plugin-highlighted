package net.mokatech.plugins.highlighted;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

import static com.intellij.openapi.editor.markup.HighlighterTargetArea.EXACT_RANGE;

public class HighlightAction extends AnAction {
    
    public static final String KEYNAME = "net.mokatech.plugins.highlighted";
    public static final TextAttributesKey KEY;
    public static final int LAYER = HighlighterLayer.LAST;
    
    static {
        TextAttributes attributes = new TextAttributes(
            null, JBColor.MAGENTA,
            null, null, Font.PLAIN);
        KEY = TextAttributesKey.createTextAttributesKey(KEYNAME, attributes);
    }
    
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
    
    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabledAndVisible(
            project != null && editor != null && editor.getSelectionModel().hasSelection());
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        List<Caret> carets = caretModel.getAllCarets();
        for (Caret caret : carets) {
            TextRange sel = caret.getSelectionRange();
            MarkupModel markupModel = editor.getMarkupModel();
            markupModel.addRangeHighlighter(
                KEY,
                sel.getStartOffset(), sel.getEndOffset(),
                LAYER, EXACT_RANGE);
        }
    }
}
