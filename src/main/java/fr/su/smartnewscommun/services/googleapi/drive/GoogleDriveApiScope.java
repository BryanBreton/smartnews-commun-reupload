package fr.su.smartnewscommun.services.googleapi.drive;

import com.google.api.services.drive.DriveScopes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GoogleDriveApiScope {
    DRIVE_METADATA_READONLY(DriveScopes.DRIVE_METADATA_READONLY),

    DRIVE_FILE(DriveScopes.DRIVE_FILE),

    DRIVE_READONLY(DriveScopes.DRIVE_READONLY),

    DRIVE(DriveScopes.DRIVE);

    final String googleApiScopeValue;

}
