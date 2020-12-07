package com.dhairytripathi.videocompressionapplication.utils

object CompressUtil {

    fun getFfpmpegBitrateCompressionCommand(
        bitrate: String,
        bitrateUnit: String,
        inputFilePath: String,
        outputFilePath: String
    ): Array<String> {

        val bitrateCompressionCommandTest =
            "ffmpeg -i input.webm -c:a copy -c:v vp9 -b:v 1M output.mkv\n"

        val bitrateCompressionCommand =
            arrayOf("-i", inputFilePath, "-b:v", "$bitrate$bitrateUnit", outputFilePath)

        return bitrateCompressionCommand
    }
}