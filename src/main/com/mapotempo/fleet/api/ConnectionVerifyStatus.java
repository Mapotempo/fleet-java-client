/*
 * Copyright © Mapotempo, 2018
 *
 * This file is part of Mapotempo.
 *
 * Mapotempo is free software. You can redistribute it and/or
 * modify since you respect the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Mapotempo is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Licenses for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Mapotempo. If not, see:
 * <http://www.gnu.org/licenses/agpl.html>
 */

package com.mapotempo.fleet.api;

/**
 * Connection status :
 * 0 : verify
 * 1XX : login or password error
 * 2XX : document missing
 * 3XX : unknow error
 */
public enum ConnectionVerifyStatus {
    VERIFY(0),
    LOGIN_ERROR(100),
    SERVER_ERROR_USER_MISSING(200),
    SERVER_ERROR_USER_CURRENT_LOCATION_MISSING(201),
    SERVER_ERROR_USER_SETTING_MISSING(202),
    SERVER_ERROR_META_INFO_MISSING(203),
    SERVER_ERROR_COMPANY_MISSING(204),
    UNKNOW_ERROR(300);

    private int mCode;

    ConnectionVerifyStatus(int code) {
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }
}
