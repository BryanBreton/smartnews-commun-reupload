package fr.su.smartnewscommun.services.googleapi.sheets;

import com.google.api.services.sheets.v4.SheetsScopes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GoogleSheetsApiScope {
    SPREADSHEETS(SheetsScopes.SPREADSHEETS);

    final String googleApiScopeValue;

}
