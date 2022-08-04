/*
 * Copyright (c) 2021. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package cn.xihan.lib.longan

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresPermission
import cn.xihan.lib.longan.activityresult.*

@Deprecated(
    "registerForStartActivityResult(callback) instead.",
    ReplaceWith(
        "registerForStartActivityResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForStartActivityResult"
    )
)
fun ActivityResultCaller.startActivityLauncher(callback: ActivityResultCallback<ActivityResult>) =
    registerForStartActivityResult(callback)

@Deprecated(
    "registerForStartIntentSenderResult(callback) instead.",
    ReplaceWith(
        "registerForStartIntentSenderResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForStartIntentSenderResult"
    )
)
fun ActivityResultCaller.startIntentSenderLauncher(callback: ActivityResultCallback<ActivityResult>) =
    registerForStartIntentSenderResult(callback)

@Deprecated(
    "registerForRequestPermissionResult(onGranted, onDenied, onShowRequestRationale) instead.",
    ReplaceWith(
        "registerForRequestPermissionResult(onGranted, onDenied, onShowRequestRationale)",
        "cn.xihan.lib.longan.activityresult.registerForRequestPermissionResult"
    )
)
fun ActivityResultCaller.requestPermissionLauncher(
    onGranted: () -> Unit,
    onDenied: AppSettingsScope.() -> Unit,
    onShowRequestRationale: PermissionScope.() -> Unit
): ActivityResultLauncher<String> =
    registerForRequestPermissionResult(onGranted, onDenied, onShowRequestRationale)

@Deprecated(
    "registerForRequestPermissionResult(callback) instead.",
    ReplaceWith(
        "registerForRequestPermissionResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForRequestPermissionResult"
    )
)
fun ActivityResultCaller.requestPermissionLauncher(callback: ActivityResultCallback<Boolean>) =
    registerForRequestPermissionResult(callback)

@Deprecated(
    "registerForRequestMultiplePermissionsResult(onAllGranted, onDenied, onShowRequestRationale) instead.",
    ReplaceWith(
        "registerForRequestMultiplePermissionsResult(onAllGranted, onDenied, onShowRequestRationale)",
        "cn.xihan.lib.longan.activityresult.registerForRequestMultiplePermissionsResult"
    )
)
fun ActivityResultCaller.requestMultiplePermissionsLauncher(
    onAllGranted: () -> Unit,
    onDenied: AppSettingsScope.(List<String>) -> Unit,
    onShowRequestRationale: PermissionsScope.(List<String>) -> Unit
) = registerForRequestMultiplePermissionsResult(onAllGranted, onDenied, onShowRequestRationale)

@Deprecated(
    "registerForRequestMultiplePermissionsResult(callback) instead.",
    ReplaceWith(
        "registerForRequestMultiplePermissionsResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForRequestMultiplePermissionsResult"
    )
)
fun ActivityResultCaller.requestMultiplePermissionsLauncher(callback: ActivityResultCallback<Map<String, Boolean>>) =
    registerForRequestMultiplePermissionsResult(callback)

@Deprecated(
    "registerForTakePicturePreviewResult(callback) instead.",
    ReplaceWith(
        "registerForTakePicturePreviewResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForTakePicturePreviewResult"
    )
)
fun ActivityResultCaller.takePicturePreviewLauncher(callback: ActivityResultCallback<Bitmap?>) =
    registerForTakePicturePreviewResult(callback)

@Deprecated(
    "registerForTakePictureResult(callback) instead.",
    ReplaceWith(
        "registerForTakePictureResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForTakePictureResult"
    )
)
fun ActivityResultCaller.takePictureLauncher(callback: ActivityResultCallback<Boolean>) =
    registerForTakePictureResult(callback)

@Deprecated(
    "registerForTakeVideoResult(callback) instead.",
    ReplaceWith(
        "registerForTakeVideoResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForTakeVideoResult"
    )
)
fun ActivityResultCaller.takeVideoLauncher(callback: ActivityResultCallback<Boolean>) =
    registerForTakeVideoResult(callback)

@Deprecated(
    "registerForPickContactResult(callback) instead.",
    ReplaceWith(
        "registerForPickContactResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForPickContactResult"
    )
)
fun ActivityResultCaller.pickContactLauncher(callback: ActivityResultCallback<Uri?>) =
    registerForPickContactResult(callback)

@Deprecated(
    "registerForPickContentResult(callback) instead.",
    ReplaceWith(
        "registerForPickContentResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForPickContentResult"
    )
)
fun ActivityResultCaller.pickContentLauncher(callback: ActivityResultCallback<Uri?>) =
    registerForPickContentResult(callback)

@Deprecated(
    "registerForGetContentResult(callback) instead.",
    ReplaceWith(
        "registerForGetContentResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForGetContentResult"
    )
)
fun ActivityResultCaller.getContentLauncher(callback: ActivityResultCallback<Uri?>) =
    registerForGetContentResult(callback)

@Deprecated(
    "registerForGetMultipleContentsResult(callback) instead.",
    ReplaceWith(
        "registerForGetMultipleContentsResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForGetMultipleContentsResult"
    )
)
fun ActivityResultCaller.getMultipleContentsLauncher(callback: ActivityResultCallback<List<Uri>>) =
    registerForGetMultipleContentsResult(callback)

@Deprecated(
    "registerForOpenDocumentResult(callback) instead.",
    ReplaceWith(
        "registerForOpenDocumentResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForOpenDocumentResult"
    )
)
fun ActivityResultCaller.openDocumentLauncher(callback: ActivityResultCallback<Uri?>) =
    registerForOpenDocumentResult(callback)

@Deprecated(
    "registerForOpenMultipleDocumentsResult(callback) instead.",
    ReplaceWith(
        "registerForOpenMultipleDocumentsResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForOpenMultipleDocumentsResult"
    )
)
fun ActivityResultCaller.openMultipleDocumentsLauncher(callback: ActivityResultCallback<List<Uri>>) =
    registerForOpenMultipleDocumentsResult(callback)

@Deprecated(
    "registerForOpenDocumentTreeResult(callback) instead.",
    ReplaceWith(
        "registerForOpenDocumentTreeResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForOpenDocumentTreeResult"
    )
)
fun ActivityResultCaller.openDocumentTreeLauncher(callback: ActivityResultCallback<Uri?>) =
    registerForOpenDocumentTreeResult(callback)

@Deprecated(
    "registerForCreateDocumentResult(callback) instead.",
    ReplaceWith(
        "registerForCreateDocumentResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForCreateDocumentResult"
    )
)
fun ActivityResultCaller.createDocumentLauncher(callback: ActivityResultCallback<Uri?>) =
    registerForCreateDocumentResult(callback)

@Deprecated(
    "registerForLaunchAppSettingsResult(callback) instead.",
    ReplaceWith(
        "registerForLaunchAppSettingsResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForLaunchAppSettingsResult"
    )
)
fun ActivityResultCaller.appSettingsLauncher(callback: ActivityResultCallback<Unit>) =
    registerForLaunchAppSettingsResult(callback)

@Deprecated(
    "registerForLaunchNotificationSettingsResult(callback) instead.",
    ReplaceWith(
        "registerForLaunchNotificationSettingsResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForLaunchNotificationSettingsResult"
    )
)
fun ActivityResultCaller.notificationSettingsLauncher(callback: ActivityResultCallback<Unit>) =
    registerForLaunchNotificationSettingsResult(callback)

@Deprecated(
    "registerForCropPictureResult(callback) instead.",
    ReplaceWith(
        "registerForCropPictureResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForCropPictureResult"
    )
)
fun ActivityResultCaller.cropPictureLauncher(callback: ActivityResultCallback<Uri?>) =
    registerForCropPictureResult(callback)

@Deprecated(
    "registerForEnableLocationResult(onLocationEnabled) instead.",
    ReplaceWith(
        "registerForEnableLocationResult(onLocationEnabled)",
        "cn.xihan.lib.longan.activityresult.registerForEnableLocationResult"
    )
)
fun ActivityResultCaller.enableLocationLauncher(onLocationEnabled: LocationScope.(Boolean) -> Unit) =
    registerForEnableLocationResult(onLocationEnabled)

@RequiresPermission(Manifest.permission.BLUETOOTH)
@Deprecated(
    "registerForEnableBluetoothResult(onLocationDisabled, onBluetoothEnabled) instead.",
    ReplaceWith(
        "registerForEnableBluetoothResult(onLocationDisabled, onBluetoothEnabled)",
        "cn.xihan.lib.longan.activityresult.registerForEnableBluetoothResult"
    )
)
fun ActivityResultCaller.enableBluetoothLauncher(
    onLocationDisabled: LocationScope.() -> Unit,
    onBluetoothEnabled: BluetoothScope.(Boolean) -> Unit
) =
    registerForEnableBluetoothResult(onLocationDisabled, onBluetoothEnabled)

@RequiresPermission(Manifest.permission.BLUETOOTH)
@Deprecated(
    "registerForEnableBluetoothResult(onBluetoothEnabled) instead.",
    ReplaceWith(
        "registerForEnableBluetoothResult(onBluetoothEnabled)",
        "cn.xihan.lib.longan.activityresult.registerForEnableBluetoothResult"
    )
)
fun ActivityResultCaller.enableBluetoothLauncher(onBluetoothEnabled: BluetoothScope.(Boolean) -> Unit) =
    registerForEnableBluetoothResult(onBluetoothEnabled)

@Deprecated(
    "registerForLaunchWifiSettingsResult(callback) instead.",
    ReplaceWith(
        "registerForLaunchWifiSettingsResult(callback)",
        "cn.xihan.lib.longan.activityresult.registerForLaunchWifiSettingsResult"
    )
)
fun ActivityResultCaller.launchWifiSettingsLauncher(callback: ActivityResultCallback<Unit>) =
    registerForLaunchWifiSettingsResult(callback)
