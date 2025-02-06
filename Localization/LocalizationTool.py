'''
=====================
2022.03.18
Hosung.Kim
---------------------
LocalizationTool

LanguagePack.xlsx 엑셀 파일을 읽어서 Android와 IOS에 각각 파일을 생성한다.
---------------------
1. python 설치 (3.7.13.2)
2. curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
3. python3 get-pip.py

python3 -m venv path/to/venv
source path/to/venv/bin/activate
python3 -m pip install xyz

4. pip install openpyxl
5. python3 LocalizationTool.py LanguagePack.xlsx
=====================
'''

'''
IOS

/iOS/Attendance/Resource/String/en.lproj/Common.strings
/iOS/Attendance/Resource/String/ko.lproj/Common.strings
/iOS/Attendance/Util/StringConst.swift

Android

/Android/app/src/main/res/values/strings.xml
/Android/app/src/main/res/values-en-rUS/strings.xml
/Android/app/src/main/res/values-ko-rKR/strings.xml
'''

DiRECTORY_ANDROID = "Android/app/src/main/res/"

from openpyxl import load_workbook
import os

def snakeToCamel(snakeStr):
    components = snakeStr.lstrip('_').split('_')
    return components[0] + ''.join(x.title() for x in components[1:])

def convertAndroidSpecialCharacterFormat(originStr):
    targetStr = originStr.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace('"', '&quot;').replace('“', '&quot;').replace('”', '&quot;').replace("'", "\&apos;").replace("‘", "\&apos;").replace("’", "\&apos;")
    return targetStr

# HealthApp 디렉토리로 설정
directoryAry = os.getcwd().split("/")
directory = ""
for i in directoryAry :
    directory += i + "/"
    if i == 'JapaneseEveryday' :
        break

# LanguagePack.xlsx 엑셀파일 읽기
workbook = load_workbook("LanguagePack.xlsx", data_only=True)
excelList = []
sheet = workbook['Sheet']
for row in sheet.columns :
    temp = []
    for cell in row :
        temp.append(str(cell.value))
    excelList.append(temp)
print(excelList)

STRING = 0
KOR = -1
EN = -1
TRANSLATABLE = 1
for i in range(1, len(excelList)) :
    if excelList[i][0] == "ko-rKR" :
        KOR = i
    if excelList[i][0] == "en-rUS" :
        EN = i
    if excelList[i][0] == "translatable" :
        TRANSLATABLE = i

# ------------------------------rule-----------------------------------
# '주석' : 주석 표시
# 'None' : 빈값 -> 편의상 enter로 치환
# 'translatable' : false일 경우 다른 언어 파일 제공 X. common file에만 데이터 들어감 (Android만 적용)
# xlsx에는 SnakeCase로 변수 선언. -> iOS 파일 생성 시 자동으로 CamelCase로 변경됨.
# 사용가능한 특수문자. &, <, >, ", '

# ------------------------------Anrdoid-----------------------------------

## for문 주석처리. 의미없어 보였음.
# # /android/HealthApp/presentation/src/main/res/values/string.xml
# tempDir = directory + "presentation/src/main/res/values/"
# if not os.path.exists(tempDir):
#     os.makedirs(tempDir)
# data = ""
# data += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!--툴에 의해서 자동으로 generate 되는 파일입니다.-->\n<resources>\n"
# for i in range(1, len(excelList[EN])) :
#     if excelList[STRING][j] != 'None' :
#         data += f"\t<string name=\"{excelList[STRING][i]}\">{excelList[EN][i]}</string>\n"
# data += "\n</resources>"
# f = open(tempDir + 'string.xml', 'w')
# f.write(data)
# f.close()

# EN, KO
# /Attendance/Android/app/src/main/res/
for i in range(2, len(excelList)) :
    if excelList[i][0] == 'None' :
        break
    tempDir = directory + "Android/app/src/main/res/values-" + excelList[i][0] + "/"
    if not os.path.exists(tempDir) :
        os.makedirs(tempDir)
    data = ""
    data += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!--툴에 의해서 자동으로 generate 되는 파일입니다.-->\n<resources>\n"
    for j in range(1, len(excelList[i])) :
        if excelList[STRING][j].strip() == '주석'  :
            data += f"\t<!-- {excelList[EN][j]} -->\n"
        elif excelList[STRING][j] == 'None' :
            data += f"\n"
        elif excelList[TRANSLATABLE][j].lower() != 'false' :
            data += f"\t<string name=\"{excelList[STRING][j]}\">{convertAndroidSpecialCharacterFormat(excelList[i][j])}</string>\n"
    data += "\n</resources>"
    f = open(tempDir + 'strings.xml', 'w')
    f.write(data)
    f.close()

# common file.
# /Attendance/Android/app/src/main/res/
tempDir = directory + "Android/app/src/main/res/values/"
if not os.path.exists(tempDir) :
    os.makedirs(tempDir)
data = ""
data += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!--툴에 의해서 자동으로 generate 되는 파일입니다.-->\n<resources>\n"
for j in range(1, len(excelList[KOR])) :
    if excelList[STRING][j].strip() == '주석' :
        data += f"\t<!-- {excelList[EN][j]} -->\n"
    elif excelList[STRING][j] == 'None' :
        data += f"\n"
    else :
        data += f"\t<string name=\"{excelList[STRING][j]}\""
        if excelList[TRANSLATABLE][j].lower() == 'false' : data += f" translatable=\"false\""
        data += f">{convertAndroidSpecialCharacterFormat(excelList[KOR][j])}</string>\n"
data += "\n</resources>"
f = open(tempDir + 'strings.xml', 'w')
f.write(data)
f.close()

print("Localization Tool Is Finished Successfully.")

