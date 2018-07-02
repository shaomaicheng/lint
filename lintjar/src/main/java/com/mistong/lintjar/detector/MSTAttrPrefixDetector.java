package com.mistong.lintjar.detector;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.mistong.lintjar.LintConfig;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_ATTR;

public class MSTAttrPrefixDetector extends Detector implements Detector.XmlScanner{
    public static final Issue ISSUE = Issue.create("AttrNotPrefixed",
            "You must prefix your custom attr",
            "We prefix all our attrs to avoid clashes.",
            Category.TYPOGRAPHY,
            5,
            Severity.WARNING,
            new Implementation(MSTAttrPrefixDetector.class,
                    Scope.RESOURCE_FILE_SCOPE));

    private LintConfig lintConfig;

    @Override
    public void beforeCheckProject(Context context) {
        lintConfig = new LintConfig(context);
    }

    @Override
    public void beforeCheckLibraryProject(Context context) {
        lintConfig = new LintConfig(context);
    }

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return folderType.getName().equalsIgnoreCase(String.valueOf(ResourceFolderType.VALUES));
    }

    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_ATTR);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(ATTR_NAME);
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        super.visitAttribute(context, attribute);
    }

    @Override
    public void visitDocument(XmlContext context, Document document) {
        super.visitDocument(context, document);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        final Attr attr = element.getAttributeNode(ATTR_NAME);
        if (attr != null) {
            String val = attr.getValue();
            if (!val.startsWith("android:") || !val.startsWith("mst")) {
                String msg = lintConfig.getConfig("attr-prefix-message");
                context.report(ISSUE, attr, context.getLocation(attr), msg);
            }
        }
        super.visitElement(context, element);
    }

    @Override
    public void visitElementAfter(XmlContext context, Element element) {
        super.visitElementAfter(context, element);
    }
}