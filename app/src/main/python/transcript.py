from youtube_transcript_api import YouTubeTranscriptApi

from youtube_transcript_api.formatters import JSONFormatter


import json

def iterateOverJSON(jsonArray):
    result = ""
    jArray = json.loads(jsonArray)
    for element in jArray:
        result += element['text'].replace("\n"," ") + " "
    return result
def gettranscript(name):
    split_string = name.split("=")
    video_id = split_string[1]#picks the second part of the video link

    transcript = YouTubeTranscriptApi.get_transcript(video_id)
    formatter = JSONFormatter()
    JSONtranscript = formatter.format_transcript(transcript)
    text = iterateOverJSON(JSONtranscript)

    return text
