package com.academy.manager.data.db

import androidx.room.TypeConverter
import com.academy.manager.data.db.entity.AttendanceStatus
import com.academy.manager.data.db.entity.FeeKind
import com.academy.manager.data.db.entity.StudentStatus
import com.academy.manager.data.db.entity.TeacherStatus

class Converters {

    @TypeConverter
    fun fromStudentStatus(value: StudentStatus): String = value.name

    @TypeConverter
    fun toStudentStatus(value: String): StudentStatus = StudentStatus.valueOf(value)

    @TypeConverter
    fun fromTeacherStatus(value: TeacherStatus): String = value.name

    @TypeConverter
    fun toTeacherStatus(value: String): TeacherStatus = TeacherStatus.valueOf(value)

    @TypeConverter
    fun fromAttendanceStatus(value: AttendanceStatus): String = value.name

    @TypeConverter
    fun toAttendanceStatus(value: String): AttendanceStatus = AttendanceStatus.valueOf(value)

    @TypeConverter
    fun fromFeeKind(value: FeeKind): String = value.name

    @TypeConverter
    fun toFeeKind(value: String): FeeKind = FeeKind.valueOf(value)
}
