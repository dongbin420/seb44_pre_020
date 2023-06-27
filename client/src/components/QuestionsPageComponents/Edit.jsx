import { useState } from "react";
import { patchRequest } from "../../api/api";
import { year, formattedMonth, formattedDay } from "../../assets/strings/date";
import TipTap from "../TipTap";

const Edit = ({
  isEditOpen,
  setIsEditOpen,
  id,
  handleContentUpdate,
  content,
}) => {
  const randomData = content;

  const [editedBody, setEditedBody] = useState(randomData);

  // 6. 질문 및 답변 patch 요청 o
  const handleFormSubmit = (e) => {
    e.preventDefault();

    const newEditedData = {
      content: editedBody,
    };
    if (newEditedData.body === "") {
      alert("최소 한 글자이상을 작성해주세요.");
    } else {
      patchRequest(
        `http://138.2.62.18:8080/answers/${id}`, // id로 변경할 데이터 명시하기 ${id}
        newEditedData
      );
      setIsEditOpen(!isEditOpen);
      handleContentUpdate(editedBody);
    }
  };

  return (
    <div>
      <form onSubmit={handleFormSubmit}>
        <TipTap setBody={setEditedBody} content={randomData} />
        <div className="flex justify-end">
          <button className="bg-sky-500 hover:bg-sky-600 p-2.5 rounded text-white text-sm">
            Complete!
          </button>
        </div>
      </form>
    </div>
  );
};

export default Edit;

// 각 질문, 답변에 맞게 edit버튼을 눌렀을때 내용이 불러와져야함.
