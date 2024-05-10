package net.mokatech.plugins.highlighted;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UnHighlightAction extends AnAction {
    
    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
    
    @Override
    public void update(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        boolean visible = project != null && editor != null;
        if (visible) {
            visible = false;
            List<Caret> carets = editor.getCaretModel().getAllCarets();
            RangeHighlighter[] allHighlighters = editor.getMarkupModel().getAllHighlighters();
            outerLoop:
            for (RangeHighlighter highlighter : allHighlighters) {
                if (!HighlightAction.KEY.equals(highlighter.getTextAttributesKey())) {continue;}
                TextRange textRange = highlighter.getTextRange();
                for (Caret caret : carets) {
                    if (textRange.containsOffset(caret.getOffset())) {
                        visible = true;
                        break outerLoop;
                    }
                }
            }
        }
        event.getPresentation().setEnabledAndVisible(visible);
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (project != null && editor != null) {
            MarkupModel markupModel = editor.getMarkupModel();
            List<Caret> carets = editor.getCaretModel().getAllCarets();
            RangeHighlighter[] allHighlighters = editor.getMarkupModel().getAllHighlighters();
            for (RangeHighlighter highlighter : allHighlighters) {
                TextRange textRange = highlighter.getTextRange();
                for (Caret caret : carets) {
                    if (textRange.contains(caret.getOffset())) {
                        markupModel.removeHighlighter(highlighter);
                    }
                }
            }
        }
    }
}
