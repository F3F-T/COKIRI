/**
 * 질문)
 * 메시지룸은 어느 시점에 생성되나? 메시지만 날려도 바로 생성되게 하는건가?
 *
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

import React, { useEffect, useRef, useState } from 'react';
import styles from '../../styles/talk/kokiriTalk.module.scss';
import { useLocation } from 'react-router-dom';
import Api from '../../utils/api';
import TalkList2 from '../../component/talk/TalkList2';
import TalkCard2 from '../../component/talk/TalkCard2';
import timeConvert from '../../utils/timeConvert';
import { useSelector } from 'react-redux';
import { Rootstate } from '../../index';
import Message2 from '../../component/talk/Message2';

import { HiPencil } from 'react-icons/hi';


// 회원이 속한 메시지룸 조회 API : /user/messageRooms API에서 가져올 데이터 interface
interface MessageRoomInfo {
  messageRoomId: number,
  authorId: number,
  lastMsg: string,
  createdDate: string,
  buyerNickname: string,
  sellerNickname: string,
  partnerName: string,
  buyerId: number,
  sellerId: number,
  postId: number
  partnerId?: number,
  // buyerThumbnail : string

}

interface ContentInfo {
  id: number,
  senderNickname: string,
  receiverNickname: string,
  content: string,
  senderId: number,
  buyerId: number,
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
  const scrollRef = useRef();
  const store = useSelector((state: Rootstate) => state);
  // console.log(state);
  //왼쪽 roomList를 담는 state
  const [roomList, setRoomList] = useState<MessageRoomInfo[]>();
  //왼쪽 roomList를 클릭헀을때 오른쪽 밑에 필요한 단일 메시지룸의 쪽지들의 정보들이 담김
  const [contentInfo, setContentInfo] = useState<ContentInfo[]>();
  //오른쪽 위 post 정보
  const [talkCardInfo, setTalkCardInfo] = useState<TalkCardInfo>();
  const [isClicked, setIsClicked] = useState<number>();
  const [initialRoom, setInitialRoom] = useState<boolean>();

  const [count, setCount] = useState(0);

  const [input, setInput] = useState('');

  let talkCardChanged = 0;
  let roomList_original: MessageRoomInfo[];

  let localIsClicked;
  const [initialRender, setInitialRender] = useState(true);

  //최초 게시글 정보 불러오기
  //오른쪽 위 post 정보를 불러옴
  function getInitialTalkCardInfo() {
    if (state) { //게시글에서 코키리톡으로 교환하기 버튼을 눌렀을때

      setTalkCardInfo(prevState => {
        let jsonObj = {
          productImg: state.post.images[0],
          tradeStatus: state.post.tradeStatus,
          title: state.post.title,
          tradeCategory: state.post.productCategory,
          wishCategory: state.post.wishCategory,
          postId: state.post.id,
          price: state.post.price,
          authorId: state.post.userInfoWithAddress.userDetail.id,
        };
        return jsonObj;
      });
    } else {
      //코끼리톡 버튼을 직접 들어갔을때

      setTalkCardInfo(prevState => {
        let jsonObj = {
          productImg: state.post.images[0],
          tradeStatus: state.post.tradeStatus,
          title: state.post.title,
          tradeCategory: state.post.productCategory,
          wishCategory: state.post.wishCategory,
          postId: state.post.id,
          price: state.post.price,
          authorId: state.post.userInfoWithAddress.userDetail.id,
        };
        return jsonObj;
      });
    }

  }

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
        authorId: state.post.userInfoWithAddress.userDetail.id,
      };
      return jsonObj;
    });
  }

  //왼쪽 메시지룸 리스트를 갖고오는 api 호출
  async function getMessageRoom() {
    try {
      const res = await Api.get('/user/messageRooms');
      console.log(res);
      roomList_original = res.data.content;
      changeMessageRoomState(roomList_original);

    } catch (err) {
      console.log(err);
      alert('getMessageRoom 조회 실패');
    }
  }

  useEffect(() => {
    getMessageRoom();
  }, [count]);

  useEffect(() => {
    console.log(contentInfo);
    if (contentInfo != null) {
      setTimeout(() => {
        // @ts-ignore
        scrollRef.current.lastChild.scrollIntoView({ behavior: 'smooth', block: 'end', inline: 'nearest' });
      }, 100);
    }
  }, [contentInfo]);

  // 메세지를 보내면 새로고침

  let foundRoom = false;

  //getMessageRoom에서 비동기적으로 처리된 roomList를 접근해서 오른쪽의 messageContent를 띄우기 위해 사용
  useEffect(() => {
    //첫번째 렌더때만 아래 코드 실행, 그 이외 roomList가 변화할때는 호출하지 않기 (isClicked 가 변화함에 따라 데이터 변환)
    if (initialRender) {
      getInitialTalkCardInfo();
      if (roomList && roomList.length > 0) {
        if (state != null) //게시글에서 "코끼리톡으로 교환하기" 버튼을 클릭해서 들어온 경우
        {
          let authorIdInState = state.post.userInfoWithAddress.userDetail.id;
          let postId = state.post.id;
          //state에 있는 유저 id와 roomlist의 authorid를 비교
          roomList.forEach(function(item, idx) {
            if (authorIdInState === item.authorId && postId === item.postId) {
              //둘다 검증된다면 만들어진 방이 존재한다는것, 새로운 빈 껍질 UI 만들지 않기
              console.log('--------');
              console.log(isClicked);
              setInitialRoom(false);
              setIsClicked(idx);
              localIsClicked = idx;
              foundRoom = true;
              return false;
            }

            //상대방 닉네임 확인
          });
          if (!foundRoom) {
            //빈 껍질 UI 만들기
            setInitialRoom(true);
            setIsClicked(-1);
          } else {
            getMessageContent(roomList[localIsClicked].messageRoomId);
          }
        } else { //일반 코끼리톡 버튼을 클릭해서 들어온 경우 : 맨 위에 있는 쪽지방 선택
          getMessageContent(roomList[0].messageRoomId); //맨 위에 있는(최신순) 쪽지방을 선택하고, 메시지를 띄워준다
          setIsClicked(0);
        }

      }

    }
    //sendMessage를 해서 렌더링이 최초가 아닐때
    else {
      console.log(roomList);
      console.log(isClicked);
      getMessageContent(roomList[isClicked].messageRoomId);
    }
  }, [roomList]);

  useEffect(() => {
    if (talkCardChanged > 0) {
      getTalkCardInfo();
    }
  }, [talkCardChanged]);

  //roomlist에서 검증할 부분이 많으니 객체에 추가를 해서 UI에서 map으로 한번에 띄워주겠다.
  const changeMessageRoomState = (originalRoomList) => {
    let partnerNickname;
    let partnerId;

    setRoomList(prevState => {
      let newRoomList = [...originalRoomList];
      newRoomList.forEach(room => {
        if (store.userInfoReducer.nickname === room.sellerNickname) {
          partnerNickname = room.buyerNickname;
        } else if (store.userInfoReducer.nickname === room.buyerNickname) {
          partnerNickname = room.sellerNickname;
        }

        //partnerId 추가
        if (store.userInfoReducer.id === room.sellerId) {
          partnerId = room.buyerId;
        } else if (store.userInfoReducer.id === room.buyerId) {
          partnerId = room.sellerId;
        }
        //TODO: buyeruserId랑 sellerUserId 동준이가 만들면 partneruserId 만들기
        room['partnerName'] = partnerNickname;
        room['partnerId'] = partnerId;
      });
      return newRoomList;
    });
  };

  async function getMessageContent(messageRoomId) {
    try {
      const res = await Api.get(`/messageRooms/${messageRoomId}`);

      const array = [...res.data];
      const reversedArray = array.reverse();

      setContentInfo(() => {
        return [...reversedArray];
      });

    } catch (err) {
      console.log(err);
      alert('메세지룸 내용 조회 실패 in kokiritalk');
    }
  }


  //왼쪽 talkList를 클릭했을때
  const onClickTalkList = (messageRoomId, idx) => {
    return (event: React.MouseEvent) => {
      // setCount(prevState => prevState+1);
      getMessageContent(messageRoomId);
      setIsClicked(idx);
      localIsClicked = idx;
      talkCardChanged++;
      //TODO: 의성) talkCard의 정보들이 roomList가 변할때마다 postId로 오른쪽 위의 게시글정보를 띄워준다. 동준이한테 postId 뿐만아니라 여기에 필요한 post DTO를 다 넘겨달라고 하면 좋을듯 아니면 post api를 사용해야함
      //TODO: 원래는 setKey로 식별후에 talkCard component에서 api를 호출해서 문제가 되었다. 그냥 부모에서 정보를 다 갖고 자식에선 api를 호출하지 않는 방식으로 구현함
      // console.log(contentInfo);
      event.preventDefault();
    };
  };

  const onChangeMessage = (e) => {
    const inputMessage = e.target.value;
    setInput(inputMessage);
    return setInput;
  };

  const createMessageRoom = async () => {
    try {
      console.log('asdff');
      const postId = state.post.id;
      //buyerid는 코키리톡으로 교환하기를 클리릭한 자신임
      const buyerId = store.userInfoReducer.id;
      const res = await Api.post(`/post/${postId}/messageRooms`, { postId, buyerId });
      return res.data;
      console.log(res);
    } catch (err) {
      console.log(err);
    }

  };
  const sendMessage = async () => {
    setInitialRender(prevState => false);
    console.log('sendMessage isClicked : ', isClicked);
    try {
      if (isClicked != -1) //새로운 방이 생성되지 않은경우
      {
        console.log('sendMessage isClicked : 2', isClicked);
        let messageInfo = {
          content: input,
          senderId: store.userInfoReducer.id,
          receiverId: roomList[isClicked].partnerId, //TODO: 동준이 partnerUserId구현되면 하기
          postId: roomList[isClicked].postId, //TODO: 동준이 postID슈 구현 되면 하기
          messageRoomId: roomList[isClicked].messageRoomId,
        };
        let messageRoomId = roomList[isClicked].messageRoomId;
        const res = await Api.post(`/messageRooms/${messageRoomId}`, messageInfo);
        console.log('메세지 전송 새로운 방이 생성되지 않은 경우', res.data);
      } else { //새로운 방이 생성된경우
        console.log('sendMessage isClicked:3새로운방생성', isClicked);
        const newMessageRoomInfo = await createMessageRoom();
        console.log(newMessageRoomInfo);

        let messageInfo = {
          content: input,
          senderId: store.userInfoReducer.id,
          receiverId: state.post.userInfoWithAddress.userDetail.id,
          postId: newMessageRoomInfo.postId, //TODO: 동준이 postID구현 되면 하기
          messageRoomId: newMessageRoomInfo.id,
        };

        const res = await Api.post(`/messageRooms/${newMessageRoomInfo.id}`, messageInfo);
        console.log('메세지 전송2', res.data);
      }

      setCount(prevState => prevState + 1);
      localIsClicked = 0;
      setIsClicked(0);


    } catch (err) {
      console.log(err);
      alert('메세지전송  실패');
    }
  };

  const onClickInitialRoom = () => {
    return (event: React.MouseEvent) => {
      setIsClicked(-1);
      localIsClicked = -1;
      talkCardChanged++;
      event.preventDefault();
    };
  };

  const onClickPencil = async () => {
    await sendMessage();
    setIsClicked(0);
    setInput('');
  };


  if (!roomList) {
    return null;
  }

  // if (!talkCardInfo) {
  //   return null;
  // }
  if (foundRoom) {
    if (!contentInfo) {
      return null;
    }
  }
  // console.log('----------state---');
  //
  // console.log(state);

  return (
    <div className={styles.wrap}>
      <div className={styles.kokiritalk}>
        <div className={styles.left}>
          <div className={styles.leftHeader}>코끼리톡</div>
          <div className={styles.left2}>
            <div className={styles.talkContainer}>
              {isClicked === -1 &&
              initialRoom ?
                <div className={styles.wrapperOn}>
                  <TalkList2 partner={state.post.userInfoWithAddress.userDetail.nickname}
                             lastContent={''}
                             date={'방금전'}
                             onClick={onClickInitialRoom()} />
                </div>
                : ''
              }
              {isClicked != -1 &&
              initialRoom ?
                <div>
                  <TalkList2 partner={state.post.userInfoWithAddress.userDetail.nickname}
                             lastContent={''}
                             date={'방금전'}
                             onClick={onClickInitialRoom()} />
                </div>
                : ''
              }
              {roomList.map((room, idx) => (
                (
                  //클릭시 border 주기
                  (isClicked === idx && <div className={styles.wrapperOn} key={room.messageRoomId}>
                    <TalkList2 keys={room['messageRoomId']}
                               partner={room['partnerName']}
                               lastContent={room['lastMsg']}
                               date={timeConvert(room['createdDate'])}
                               onClick={onClickTalkList(room['messageRoomId'], idx)} />

                  </div>) ||
                  (isClicked != idx && <div key={room.messageRoomId}>
                    <TalkList2 keys={idx}
                               partner={room['partnerName']}
                               lastContent={room['lastMsg']}
                               date={timeConvert(room['createdDate'])}
                               onClick={onClickTalkList(room['messageRoomId'], idx)} />
                  </div>)
                )
              ))}
            </div>
          </div>
        </div>

        <div className={styles.right}>
          <div className={styles.right_headerBox}>
            <div className={styles.right_header}>
              <div className={styles.right_header1}>
                <div className={styles.right_header1_1}><TalkCard2 talkCardInfo={talkCardInfo} /></div>
                {/*<div className={styles.right_header1_1}>동준이의 postid 가 구현되어야 가능한 기능</div>*/}
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
          <div ref={scrollRef} className={styles.talkContainer2}>
            {//최초 생성된 방이 클릭된 경우 메세지를 null로 설정
              isClicked === -1 && ''
            }
            {//최초 생성된 방이 아닌 경우, 또는 최초 생성된 방의 동선이 아닌경우
              isClicked != -1 && <Message2 contentInfo={contentInfo} />
            }
          </div>
          <div className={styles.writeComments}>
            <input type={'text'} className={styles.writeInput} placeholder={'쪽지를 보내세요'} value={input}
                   onChange={onChangeMessage} />
            <HiPencil className={styles.pencilIcon} onClick={onClickPencil} />
          </div>
        </div>
      </div>
      {/*<Footer/>*/}
    </div>
  );
};


export default KokiriTalk2;