/**
 * 코끼리 톡 구현 로직
 *
 * Default )
 * API 사용 )
 * 1.(조회)회원이 속한 메시지룸 조회
 *  a.authorid로 본인 게시글인지 검증
 *  본인 게시글일때)
 *  메시지 룸을 들어갔을떄 게시글 tradeStatus 변경 가능, 보낸쪽지, 받은 쪽지가 구별되어야함
 *  본인 게시글이 아닐때)
 *  메시지 룸을 들어갔을떄 게시글 tradeStatus 변경 불가능,
 *
 *  - tradeStatus 변경
 *  - 받은 쪽지, 보낸쪽지 구별
 *
 * 2.채팅방 쪽지 전체 조회
 *
 *
 * 리덕스 store에 저장되어있는 userInfo를 통해
 *
 * 동선 1) 개시글에서 코끼리톡으로 이동했을때
 * PostDetail의 게시글 정보가 담겨있는 post state 를 navigate로 같이 전달한다
 *
 * ! 어떻게 동선에 따라 오른쪽에 있는 쪽지방의 content를 띄울지 고민해봐야함
 *
 */

import React, { useEffect, useState } from 'react';
import styles from '../../styles/talk/kokiriTalk.module.scss';
import { useLocation } from 'react-router-dom';
import Api from '../../utils/api';
import TalkList from '../../component/talk/TalkList';
import timeConvert from '../../utils/timeConvert';
import { useSelector } from 'react-redux';
import { Rootstate } from '../../index';
import Message2 from '../../component/talk/Message2';
import TalkCard2 from '../../component/talk/TalkCard2';


// 회원이 속한 메시지룸 조회 API : /user/messageRooms API에서 가져올 데이터 interface
interface MessageRoomInfo {
  messageRoomId: number,
  authorId: number,
  lastMsg: string,
  createdDate: string,
  buyerNickname: string,
  sellerNickname: string,
  partnerName: string,
  // buyerThumbnail : string

}

interface ContentInfo {
  id: number,
  senderNickname: string,
  receiverNickname: string,
  content: string,
  senderId: number,
  receiverId: number,
  messageRoomId: number
  createTime: string;
}

//위의 post 관련 info
interface TalkCardInfo {
  productImg: string,
  tradeStatus: string,
  title: string,
  tradeCategory: string,
  wishCategory: string
  postId: number,
  price: number,
  authorId: number
}


const KokiriTalk2 = () => {
  const { state } = useLocation();
  const store = useSelector((state: Rootstate) => state);
  console.log(state);
  //왼쪽 roomList를 담는 state
  const [roomList, setRoomList] = useState<MessageRoomInfo[]>();
  //왼쪽 roomList를 클릭헀을때 오른쪽 밑에 필요한 단일 메시지룸의 쪽지들의 정보들이 담김
  const [contentInfo, setContentInfo] = useState<ContentInfo[]>();
  //오른쪽 위 post 정보
  const [talkCardInfo, setTalkCardInfo] = useState<TalkCardInfo>();
  let roomList_original: MessageRoomInfo[];

  //오른쪽 위 post 정보를 불러옴
  function getTalkCardInfo() {
    setTalkCardInfo(prevState => {
      let jsonObj = {
        productImg: state.post.images[0],
        tradeStatus: state.post.tradeStatus,
        title: state.post.title,
        tradeCategory: state.post.productCategory,
        wishCategory: state.post.wishCategory,
        postId: state.post.id,
        price: state.post.price,
        authorId: state.post.userInfoWithAddress.id,
      };
      return jsonObj;
    });

  }

  //왼쪽 메시지룸 리스트를 갖고오는 api 호출
  async function getMessageRoom() {

    try {
      const res = await Api.get('/user/messageRooms');

      roomList_original = res.data.content;
      console.log(roomList_original);
      changeMessageRoomState(roomList_original);

    } catch (err) {
      console.log(err);
      alert('getMessageRoom 조회 실패');
    }
  }

  useEffect(() => {
    getMessageRoom();
    if (state) {
      getTalkCardInfo();
    } else {
      //게시글에서 바로 이동하지 않았을떄, 제일 최신의 채팅방의 postId 정보를 이용해서 띄움
    }
  }, []);

  //roomlist에서 검증할 부분이 많으니 객체에 추가를 해서 UI에서 map으로 한번에 띄워주겠다.
  const changeMessageRoomState = (originalRoomList) => {
    let partner;
    setRoomList(prevState => {
      let newRoomList = [...originalRoomList];
      newRoomList.forEach(room => {
        if (store.userInfoReducer.nickname === room.sellerNickname) {
          partner = room.buyerNickname;
        } else if (store.userInfoReducer.nickname === room.buyerNickname) {
          partner = room.sellerNickname;
        }
        room['partnerName'] = partner;
      });
      return newRoomList;
    });
  };

  async function getMessageContent(messageRoomId) {
    try {
      const res = await Api.get(`/messageRooms/${messageRoomId}`);

      setContentInfo(() => {
        return [...res.data];
      });

    } catch (err) {
      console.log(err);
      alert('메세지룸 내용 조회 실패 in kokiritalk');
    }
  }


  //왼쪽 talkList를 클릭했을때
  const onClickTalkList = (messageRoomId) => {
    return (event: React.MouseEvent) => {
      // setCount(prevState => prevState+1);
      getMessageContent(messageRoomId);
      //TODO: 원래는 setKey로 식별후에 talkCard component에서 api를 호출해서 문제가 되었다. 그냥 부모에서 정보를 다 갖고 자식에선 api를 호출하지 않는 방식으로 구현함

      event.preventDefault();
    };
  };

  console.log(roomList);
  if (!roomList) {
    return null;
  }

  if (!talkCardInfo) {
    return null;
  }

  console.log(talkCardInfo);


  // @ts-ignore
  return (
    <div className={styles.wrap}>
      <div className={styles.kokiritalk}>
        <div className={styles.left}>
          <div className={styles.leftHeader}>코끼리톡</div>
          <div className={styles.left2}>
            <div className={styles.talkContainer}>
              {roomList.map((room) => (
                <div className={styles.wrapper} key={room.messageRoomId}>
                  <TalkList keys={room['messageRoomId']}
                            partner={room['partnerName']}
                            lastContent={room['lastMsg']}
                            date={timeConvert(room['createdDate'])}
                            onClick={onClickTalkList(room['messageRoomId'])} />

                </div>
              ))}
            </div>
          </div>
        </div>


        <div className={styles.right}>
          <div className={styles.right_headerBox}>
            <div className={styles.right_header}>
              <div className={styles.right_header1}>
                <div className={styles.right_header1_1}><TalkCard2 talkCardInfo={talkCardInfo} /></div>
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
            <Message2 contentInfo={contentInfo} />
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