import React from 'react';
import styles from '../../styles/talk/kokiriTalk.module.scss';


interface contentInfo {
  id: number,
  senderNickname: string,
  receiverNickname: string,
  content: string,
  senderId: string,
  receiverId: string,
  messageRoomId: number
}

interface contentInfo2 {
  id: number,
  senderNickname: string,
  receiverNickname: string,
  content: string,
  senderId: string,
  receiverId: string,
  messageRoomId: number
}


const KokiriTalk2 = () => {

  return (
    <div className={styles.wrap}>
      <div className={styles.kokiritalk}>
        <div className={styles.left}>
          <div className={styles.leftHeader}>코끼리톡</div>
          <div className={styles.left2}>
            <div className={styles.talkContainer}>
              {/*{roomList.map((SingleObject: object) => (*/}

              {/*  SingleObject['messageRoomId'] === talkCard.id ?//눌렸냐*/}
              {/*    SingleObject['buyerNickname'] === info.nickname ?*/}
              {/*      // <>눌렸는데 구매자</>*/}
              {/*      SingleObject['buyerDelStatus'] === false ?*/}

              {/*        <div className={styles.wrapper}>*/}
              {/*          <TalkList keys={SingleObject['messageRoomId']}*/}
              {/*                    partner={SingleObject['sellerNickname']}*/}
              {/*                    lastCo작ntent={SingleObject['lastMsg']}*/}
              {/*                    date={timeConvert(SingleObject['createdDate'])}*/}
              {/*                    onClick={onClickTotalTalkList(SingleObject['messageRoomId'])}*/}
              {/*                    counts={count} />*/}
              {/*        </div>*/}
              {/*        :*/}
              {/*        <></>*/}
              {/*      :*/}
              {/*      // <>눌렸는데 판매자</>*/}
              {/*      SingleObject['sellerDelStatus'] === false ?*/}

              {/*        <div className={styles.wrapper}>*/}
              {/*          <TalkList keys={SingleObject['messageRoomId']}*/}
              {/*                    partner={SingleObject['buyerNickname']}*/}
              {/*                    lastContent={SingleObject['lastMsg']}*/}
              {/*                    date={timeConvert(SingleObject['createdDate'])}*/}
              {/*                    onClick={onClickTotalTalkList(SingleObject['messageRoomId'])}*/}
              {/*                    counts={count} />*/}
              {/*        </div>*/}
              {/*        :*/}
              {/*        <></>*/}
              {/*    :*/}
              {/*    // <>안눌림</>*/}
              {/*    SingleObject['buyerNickname'] === info.nickname ?*/}
              {/*      SingleObject['buyerDelStatus'] === false ?*/}
              {/*        <TalkList keys={SingleObject['messageRoomId']}*/}
              {/*                  partner={SingleObject['sellerNickname']}*/}
              {/*                  lastContent={SingleObject['lastMsg']}*/}
              {/*                  date={timeConvert(SingleObject['createdDate'])}*/}
              {/*                  onClick={onClickTotalTalkList(SingleObject['messageRoomId'])}*/}
              {/*                  counts={count} />*/}
              {/*        :*/}
              {/*        <></>*/}
              {/*      :*/}
              {/*      // <>눌렸는데 판매자</>*/}
              {/*      SingleObject['sellerDelStatus'] === false ?*/}

              {/*        <TalkList keys={SingleObject['messageRoomId']} partner={SingleObject['buyerNickname']}*/}
              {/*                  lastContent={SingleObject['lastMsg']}*/}
              {/*                  date={timeConvert(SingleObject['createdDate'])}*/}
              {/*                  onClick={onClickTotalTalkList(SingleObject['messageRoomId'])}*/}
              {/*                  counts={count} />*/}
              {/*        :*/}
              {/*        <></>*/}

              {/*))}*/}
            </div>
          </div>
        </div>


        <div className={styles.right}>
          <div className={styles.right_headerBox}>
            <div className={styles.right_header}>
              <div className={styles.right_header1}>
                {/*<div className={styles.right_header1_1}><TalkCard keys={key} /></div>*/}
                <div className={styles.right_header1_1}>"ㅗhih</div>
              </div>
              <div className={styles.right_header2}>
                <button className={styles.sideBtn} onClick={() => {
                  // deleteRoom();
                }}>삭제
                </button>
                <p> | </p>
                <button className={styles.sideBtn}>차단</button>
                <p> | </p>
                <button className={styles.sideBtn}>신고</button>
              </div>
            </div>
            {/*<div className={styles.right_header1_2}>{talkCard.opponentNickname}님과의 쪽지방입니다.</div>*/}
            <div className={styles.right_header1_2}>님과의 쪽지방입니다.</div>
          </div>
          <div className={styles.talkContainer2}>
            {/*{key === null ?*/}
            {/*  <></> : <Message keys={key} counts={count} />*/}
            {/*}*/}
          </div>
          {/*<div ref={scrollRef} className={styles.writeComments}>*/}
          {/*  <input type={'text'} className={styles.writeInput} placeholder={'쪽지를 보내세요'} value={input}*/}
          {/*         onChange={onChangeMessage} />*/}
          {/*  <HiPencil className={styles.pencilIcon} onClick={() => {*/}
          {/*    createMessageRoom();*/}
          {/*    setCount(prevState => prevState + 1);*/}
          {/*    setInput('');*/}
          {/*  }} />*/}
          {/*</div>*/}
        </div>
      </div>
      {/*<Footer/>*/}
    </div>
  );
};


export default KokiriTalk2;