package com.example.zooseeker25;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class LocationPermissionChecker {
    private ComponentActivity activity;

    final ActivityResultLauncher<String[]> requestPermissionLauncher;

    public LocationPermissionChecker(ComponentActivity activity) {
        this.activity = activity;
        requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
            perms.forEach((perm, isGranted) -> {
                Log.d("Zooseeker", String.format("Permission %s granted: %s", perm, isGranted));
            });
        });
    }

    //prompts the user to give/deny permissions to track their location
    public boolean ensurePermissions() {
        String[] requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean hasNoLocationPerms = Arrays.stream(requiredPermissions)
                .map(perm -> ContextCompat.checkSelfPermission(activity, perm))
                .allMatch(status -> status == PackageManager.PERMISSION_DENIED);

        if (hasNoLocationPerms) {
            requestPermissionLauncher.launch(requiredPermissions);
            // Note: the activity will be restarted when permission change!
            // This entire method will be re-run, but we won't get stuck here.
            return true;
        }
        return false;
    }
}
