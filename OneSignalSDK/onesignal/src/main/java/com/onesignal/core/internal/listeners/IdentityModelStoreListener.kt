package com.onesignal.core.internal.listeners

import com.onesignal.core.internal.application.IApplicationService
import com.onesignal.core.internal.models.ConfigModelStore
import com.onesignal.core.internal.models.IdentityModel
import com.onesignal.core.internal.models.IdentityModelStore
import com.onesignal.core.internal.operations.CreateUserOperation
import com.onesignal.core.internal.operations.DeleteUserOperation
import com.onesignal.core.internal.operations.IOperationRepo
import com.onesignal.core.internal.operations.Operation
import com.onesignal.core.internal.operations.UpdateUserOperation

internal class IdentityModelStoreListener(
    store: IdentityModelStore,
    opRepo: IOperationRepo,
    private val _configModelStore: ConfigModelStore
) : ModelStoreListener<IdentityModel>(store, opRepo) {

    override fun getAddOperation(model: IdentityModel): Operation? {
        // TODO: Snapshot the model to prevent it from changing while the operation has been queued.
        return CreateUserOperation(_configModelStore.get().appId!!, model.id, null)
    }

    override fun getRemoveOperation(model: IdentityModel): Operation? {
        return DeleteUserOperation(model.id)
    }

    override fun getUpdateOperation(model: IdentityModel, property: String, oldValue: Any?, newValue: Any?): Operation? {
        return UpdateUserOperation(model.id, property, newValue)
    }
}