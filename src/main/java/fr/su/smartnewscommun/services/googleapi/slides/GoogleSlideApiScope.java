package fr.su.smartnewscommun.services.googleapi.slides;

import com.google.api.services.slides.v1.SlidesScopes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GoogleSlideApiScope {
    PRESENTATIONS(SlidesScopes.PRESENTATIONS),

    PRESENTATIONS_READONLY(SlidesScopes.PRESENTATIONS_READONLY);

    final String googleApiScopeValue;
}
