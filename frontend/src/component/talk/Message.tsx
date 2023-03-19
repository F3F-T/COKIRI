import styles from '../../styles/talk/talkList.module.scss';
import React, { useEffect, useRef, useState } from 'react';
import Api from '../../utils/api';
import { useDispatch, useSelector } from 'react-redux';
import { Rootstate } from '../../index';
import { setBuyerId, setMessageRoomId, setOpponetNick, setPostId, setSellerId } from '../../store/talkCardReducer';
import timeConvert from '../../utils/timeConvert';


interface keyProps {
  keys: number;
  counts: number;

}


// props: {key: keyProps,count:countProps}
const Message = (key: keyProps) => {
  console.log('Message');
  const scrollRef = useRef();
  const [contentInfo, setContentInfo] = useState(null);
  const dispatch = useDispatch();
  const talkCard = useSelector((state: Rootstate) => {
    return state.talkCardReducer;
  });
  const info = useSelector((state: Rootstate) => {
    return state.userInfoReducer;
  });
  const [roomList, setRoomList] = useState(null);
  const realKey = key.keys;
  const realCount = key.counts;

  useEffect(() => {

    getMessageRoom();

  }, [realKey]);

  console.log('메세지메세지메세지', talkCard.delStatus);
  useEffect(() => {
    getMessageRoom();
  }, [realCount]);
  // useEffect(() => {
  //   if (contentInfo != null) {
  //     // @ts-ignore
  //     scrollRef.current.lastChild.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
  //   }
  // }, [contentInfo]);

  //키값에 해당하는거 띄우려고 호출
  async function getMessageRoom() {
    console.log('Message component getMessageRoom');
    try {
      const res = await Api.get('/user/messageRooms');
      // console.log("메세지룸 조회", res.data.content)
      const res2 = await Api.get(`/user/${info.id}/totalMessageRooms`);
      // console.log("메세지룸 조회2",res2.data)
      // dispatch(setMessageRoomId(res.data.content.messageRoomId))

      for (let i = 0; i < res2.data.length; i++) {

        if (res2.data[i].id == realKey) {
          dispatch(setPostId(res2.data[i].postId));
          dispatch(setMessageRoomId(res2.data[i].id));
          dispatch(setBuyerId(res2.data[i].buyerId));
          dispatch(setSellerId(res2.data[i].sellerId));
          if (info.id === res2.data[i].buyerId) {
            dispatch(setOpponetNick(res2.data[i].sellerNickName));
          } else {
            dispatch(setOpponetNick(res2.data[i].buyerNickName));
          }
          getMessageContent(res2.data[i].id);

        }
      }


      setRoomList(() => {
        return [...res.data.content];
      });
      // alert("메세지룸 조회 성공 in message")
    } catch (err) {
      console.log(err);
      alert('메세지룸 조회 실패 in message');
    }
  }

  async function getMessageContent(loading) {
    try {
      const res = await Api.get(`/messageRooms/${loading}`);
      // console.log("메세지룸 내용조회 in message", res.data)
      setContentInfo(() => {
        return [...res.data];
      });


      // for(let i=0;i<res.data.length;i++){
      //     contentInfo1[i]=res.data[i]
      // }
      // console.log("바로들어간다니까",contentInfo1)
      return res.data;
    } catch (err) {
      console.log(err);
      alert('메세지룸 내용 조회 실패 in message');
    }
  }

  // const loading = await createMessageRoom();

  if (!contentInfo) {
    return null;
  }

  if (!roomList) {
    return null;
  }

  return (
    <div ref={scrollRef}>
      {contentInfo.map((a, i) => (
        contentInfo[i].senderId === info.id ?
          //TODO: 의성) map함수인데 왜 a를 안쓰고 contentInfo[i]로 한거야 a.senderId로 하면 됨
          <div className={styles.receive}>
            <div className={styles.receiveTitle}>보낸 쪽지</div>
            <div className={styles.timeBox}>
              <p className={styles.receiveContent}>{contentInfo[i].content}</p>
              <p className={styles.timeX}>{timeConvert(contentInfo[i].createTime)}</p>
            </div>
          </div>
          :
          <div className={styles.send}>
            <div className={styles.sendTitle}>받은 쪽지</div>
            <div className={styles.timeBox}>
              <p className={styles.sendContent}>{contentInfo[i].content}</p>
              <p className={styles.timeX}>{timeConvert(contentInfo[i].createTime)}</p>

            </div>
          </div>
      ))
      }
    </div>
  );
};
export default Message;