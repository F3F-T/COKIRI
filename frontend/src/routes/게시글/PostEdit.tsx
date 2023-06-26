import React, { useCallback, useEffect, useRef, useState } from 'react';
import styles from '../../styles/loginAndSignup/PostUpload.module.css';
import { useNavigate, useParams } from 'react-router-dom';
import photo from '../../img/add.png';
import Tags from '@yaireo/tagify/dist/react.tagify'; // React-wrapper file
// import "@yaireo/tagify/src/tagify.scss"
import '../../styles/scss/main.scss';
import Api from '../../utils/api';
import { Rootstate } from '../../index';
import { useSelector } from 'react-redux';
//https://github.com/yairEO/tagify 에서 tagify 참조
import { NumericFormat } from 'react-number-format';
import Select from 'react-select';
import Modal from '../로그인 & 회원가입/NeighborModal';
import { TiDelete } from 'react-icons/ti';


/**
 * 수정 로직
 * 기존에 있었던 정보들(title, content, price, category, tag 등등)을 불러와서 input form에 넣어준다
 * 사용자가 수정을 하게 되면 state를 변화시킨다
 *
 * 이미지 수정)
 * 기존에 있던 이미지를 불러와서 showImage state에 저장시킨 후 미리보기에 띄워준다
 * 이미지 추가를 한 파일만을 다른 배열에 저장시켜 post/image 서버 통신 후 url을 받아와서 저장시킨다
 */

const PostEdit = () => {
  interface PostType {
    id?: number;
    title?: string;
    content?: string;
    price: string;
    tradeEachOther?: boolean;
    authorNickname?: string;
    wishCategory?: string;
    productCategory?: string;
    tradeStatus?: string;
    tagNames?: string[];
    scrapCount?: number;
    messageRoomCount?: number;
    createdTime?: string;
    userInfo?: UserInfo;
    userInfoWithAddress: {
      userDetail: UserInfo
      address?: [
        {
          postalAddress?: string
        }
      ]
    };
    images: string[];
  }

  interface UserInfo {
    id: number;
    email: string;
    birthDate: string;
    description: string;
    imageUrl: string;
    loginType: string;
    phoneNumber: string;
    scrapId: number;
    userName: string;
    nickname: string;

  }

  interface UploadData {
    title: string;
    price: string;
    content: string;
    productCategory: String;
    wishCategory: String;
    tag: string[];
  }

  interface ModalDefaultType {
    onClickToggleModal: () => void;
  }


  const [uploadData, setUploadData] = useState<UploadData>();
  let tradeEachOther: boolean = undefined;
  const store = useSelector((state: Rootstate) => state);
  const fileInput = useRef(null);
  const [photoData, setPhotoData] = useState(null);
  const [photoUrl, setPhotoUrl] = useState<string[]>(null);
  const params = useParams();
  let changingOnlyOnce = 0;
  let photoUrlList;
  let finalImage;
  // console.log(params)


  //수정 로직
  const [post, setPost] = useState<PostType>(null);
  const [showImages, setShowImages] = useState([]);
  const [isOpenModal, setOpenModal] = useState<boolean>(false);

  const onClickToggleModal = useCallback(() => {
    setOpenModal(!isOpenModal);
  }, [isOpenModal]);

  interface ArrayObjectSelectState {
    selectedCategory: Category | null | string;
  }

  const [productState, setProductState] = React.useState<ArrayObjectSelectState>({
    selectedCategory: null,
  });

  const [wishState, setWishState] = React.useState<ArrayObjectSelectState>({
    selectedCategory: null,
  });

  const postId = params.id;
  console.log(postId);


  let fixed;

  const getPost = useCallback(async () => {
    //반복적으로 렌더링 되어 이미지 누적되는 것을 방지하는 초기화 로직
    setShowImages(prevState => {
      return [];
    });
    try {
      console.log('getPost 요청');
      const res = await Api.get(`/post/${postId}`);

      setPost(prevState => {
        return { ...prevState, ...res.data };
      });


      /**
       * 원래 post의 state를 uploadData에 복사해주는 방식으로 시도했지만 useState의 비동기적 처리때문에 uploadData에 post의 데이터를 넣어주지 못했다.
       * if(!post) return null 후에 setUploadData를 넣어주게 되면 state가 무한으로 변화하기 때문에 무한 렌더링 이슈가 생기고,
       * 무한 랜더링을 방지하기 위해 useEffect에 post 변경시에 한번만 추가하려고 했지만, 이는 또 post가 null이 뜨게 되어 그냥 res data를 직접 넣어주는 식으로 구현함
       *
       * useState, async, await, promise의 반환 등의 비동기식 처리, state 변경시 무한 렌더링, useEffect deps에 함수 추가, useEffect의 얕은 복사 -> useCallback으로 처리 등의 개념을 익힘
       */

      setUploadData({
        title: res.data.title,
        price: res.data.price,
        content: res.data.content,
        productCategory: res.data.productCategory,
        wishCategory: res.data.wishCategory,
        tag: res.data.tagNames,
      });
      console.log(changingOnlyOnce);

      //무조건 한번 실행되어야 하는 getPost가 원인모르는 리렌더링이 되어 한번 더 실행되는 경우가 있다. 이를 방지하기 위한 if문
      if (changingOnlyOnce === 0) {
        //이미지 데이터가 imgPath와 id로 들어가 있어서 imgPath만 따로 저장
        res.data.images.map((image) => (
          // setShowImages([image.imgPath])
          setShowImages(prevState => {
            return [...prevState, image];
          })
        ));
      }
      changingOnlyOnce++;

      setProductState({ selectedCategory: res.data.productCategory });
      setWishState({ selectedCategory: res.data.wishCategory });

    } catch
      (err) {
      console.log(err);
      alert('get 실패');
    }
  }, []);

  interface Category {
    name: string;
  }

  const categories: Category[] =
    [
      { name: '전체' },
      { name: '도서' },
      { name: '식품' },
      { name: '티켓/교환권' },
      { name: '의류' },
      { name: '서비스/기술' },
      { name: '유아동용품' },
      { name: '운동용품' },
      { name: '가구' },
      { name: '뷰티/미용' },
      { name: '반려동물용품' },
      { name: '식물' },
      { name: '취미/게임' },
      { name: '수집품' },
      { name: '인테리어' },
      { name: '생활/주방' },
      { name: '전자기기' },
      { name: '기타' },
      { name: '상관없음' },
    ];

  const categories2: Category[] =
    [
      { name: '도서' },
      { name: '식품' },
      { name: '티켓/교환권' },
      { name: '의류' },
      { name: '서비스/기술' },
      { name: '유아동용품' },
      { name: '운동용품' },
      { name: '가구' },
      { name: '뷰티/미용' },
      { name: '반려동물용품' },
      { name: '식물' },
      { name: '취미/게임' },
      { name: '수집품' },
      { name: '인테리어' },
      { name: '생활/주방' },
      { name: '전자기기' },
      { name: '기타' },
      { name: '상관없음' },
    ];

  const navigate = useNavigate();

  const onChangeTitle = (e) => {
    const inputTitle = e.target.value;
    setUploadData((prevState) => {
      return { ...prevState, title: inputTitle };
    });
  };

  const onChangePrice = (e, value) => {
    const inputPrice = e.target.value;

    setUploadData((prevState) => {
      return { ...prevState, price: inputPrice };
    });
  };

  const onChangeContent = (e) => {
    const inputContent = e.target.value;
    setUploadData((prevState) => {
      return { ...prevState, content: inputContent };
    });
  };

  const onChangeTag = (e) => {
    const inputTag = e.target.value;
  };

  const signUpButtonClick = () => {
    navigate(`/signup/emailcheck`);
  };

  // on tag add/edit/remove
  const onChange = useCallback((e) => {

    const tagifyCleanValue = e.detail.tagify.getCleanValue();

    let tagList = tagifyCleanValue.reduce((prev, cur) => {
      prev.push(cur.value);
      return prev;
    }, []);


    setUploadData((prevState) => {
      return { ...prevState, tag: tagList };
    });

  }, []);

  async function uploadPost(jsonObj) {

    //interceptor를 사용한 방식 (header에 token값 전달)
    try {
      const res = await Api.patch(`/post/${postId}`, jsonObj);

      alert('수정 성공');
      navigate(`/post/${postId}`);
    } catch (err) {
      console.log(err);
      alert('업로드 실패');
    }
  }

  const imageUpload = async () => {
    try {

      const res = await Api.post('/auth/image/postImage', photoData);
      return res.data.imageUrls;

    } catch (err) {
      console.log(err);
      alert('이미지 업로드 실패');
    }


  };


  const onClickUploadButton = async () => {

    if (uploadData.productCategory === uploadData.wishCategory) {
      tradeEachOther = true;

      console.log('true');
    } else {
      tradeEachOther = false;
      console.log('false');
    }


    //사진 업로드(새로 추가된 image가 있으면 post/image 로 서버에 이미지를 등록한다.)
    if (photoData) {
      photoUrlList = await imageUpload();
    }
    //추가된 이미지가 존재하여 서버에서 url 정보를 받아왔다면 기존의 서버 url image와 추가된 이미지를 finalImage에 담는다
    if (photoUrlList) {
      finalImage = [...post.images, ...photoUrlList];
    }
    //새로 추가된 이미지가 존재하지 않는다면 기존의 서버 url image만 담는다
    else {
      finalImage = [...post.images];
    }


    if (finalImage.length < 1) {
      alert('사진을 한장 이상 업로드해주세요.');
    } else {
      const jsonObj = {
        'title': uploadData.title,
        'content': uploadData.content,
        'price': uploadData.price,
        'tradeEachOther': tradeEachOther,
        'authorId': store.userInfoReducer.id,
        'productCategory': uploadData.productCategory,
        'wishCategory': uploadData.wishCategory,
        'tagNames': [...uploadData.tag],
        'images': finalImage,
        'thumbnail': finalImage[0],
      };
      uploadPost(jsonObj);
      console.log('업로드 성공');
    }


  };

  const onChangeImg = async (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();

    if (e.target.files) {
      const imageLists = e.target.files;
      let imageUrlLists = [...showImages];
      const formData = new FormData();

      for (let i = 0; i < imageLists.length; i++) {
        //미리보기 파일 imgeUrlLists에 추가
        const currentImageUrl = URL.createObjectURL(imageLists[i]);
        imageUrlLists.push(currentImageUrl);
        //API 통신을 위한 formData에도 추가
        formData.append('imageFiles', imageLists[i]);
      }


      //max를 10장으로 설정
      if (imageUrlLists.length > 10) {
        imageUrlLists = imageUrlLists.slice(0, 10);
        alert('사진 업로드는 최대 10장입니다.');
      }

      //미리보기 데이터
      setShowImages(imageUrlLists);

      //api 통신
      setPhotoData(formData);


    }
  };


  const onChangePriceSecond = (value) => {
    setUploadData((prevState) => {
      return { ...prevState, price: value };
    });
  };

  const deleteImg = (index) => {
    setShowImages((prevState) => {
      prevState.splice(index, 1);
      return [...prevState];
    });

    setPost((prevState) => {
      prevState.images.splice(index, 1);
      return { ...prevState };
    });

  };

  /**
   * 로그인 상태를 확인하고 비로그인일때 이전화면으로 돌아가기
   */
  useEffect(() => {
    if (store.jwtTokenReducer.authenticated) {
      console.log('로그인 상태');
    } else {
      alert('로그인후에 가능한 서비스입니다.');
      navigate(-1);
    }
  }, [store.jwtTokenReducer.authenticated]);


  //사진 복사 이슈때문에 무조건 렌더링 첫번째에만 실행이 되어야함
  useEffect(() => {
    getPost();
  }, []);
  // console.log(showImages)
  console.log(post);


  if (!post) {
    return null;
  }

  if (!post.images) {
    return null;
  }


  return (
    <div className={styles.postBox}>
      {isOpenModal && (
        <Modal onClickToggleModal={onClickToggleModal}>
          <embed type='text/html' width='800' height='608' />
        </Modal>
      )}
      <div className={styles.postUpload}>
        <div className={styles.header}>
          <p className={styles.header_1}>기본 정보</p>
          <p className={styles.header_2}>* 필수 정보</p>
        </div>
        <div className={styles.container}>
          <div className={styles.item1}>
            <img className={styles.photos} src={photo} onClick={() => {
              fileInput.current.click();
            }} />
            {
              showImages.map((image, id) => (
                <div className={styles.imgClass}><img className={styles.photos} alt={`${image}-${id}`} key={image.id}
                                                      src={image}
                                                      onClick={() => onClickToggleModal()} />
                  <TiDelete key={id} className={styles.imgRemoveButton} onClick={(e) => {
                    deleteImg(id);
                  }} />
                </div>
              ))
            }
            <form>
              <input type='file' style={{ display: 'none' }} multiple accept='image/*'
                     onChange={onChangeImg} ref={fileInput} />
            </form>
            <p className={styles.photoText}>최대 10장의 사진을 등록해주세요.</p>
          </div>
          <div className={styles.item2}>
            <p className={styles.star}>*</p><input type='text' className={styles.item2_2}
                                                   placeholder={'글 제목을 적어주세요.'} onChange={onChangeTitle}
                                                   value={uploadData.title} />
          </div>
          <div className={styles.item2}>
            <p className={styles.star}>*</p> <NumericFormat className={styles.item2_2}
                                                            placeholder='생각하는 물건의 가격대를 숫자로 적어주세요.'
                                                            value={uploadData.price}
                                                            prefix={'₩'} allowLeadingZeros
                                                            thousandSeparator=','
                                                            onValueChange={(values) => {
                                                              onChangePriceSecond(values.floatValue);
                                                            }} />
          </div>
          <div className={styles.contentAreaBox}>
            <p className={styles.star}>*</p>
            <textarea className={styles.contentArea} placeholder='상도1동에 올릴 게시글을 적어주세요.'
                      onChange={onChangeContent} value={uploadData.content} spellCheck={'false'} />
          </div>
          <div className={styles.categoryBox}>
            <p className={styles.star}>*</p>
            <p className={styles.categoryText}>올릴 물건의 카테고리를 선택해주세요.</p>
          </div>
          <div className={styles.categoryBox2}>
            <Select
              styles={{ // zIndex
                menu: provided => ({ ...provided, zIndex: 999 }),
              }}
              // If you don't need a state you can remove the two following lines value & onChange
              value={productState.selectedCategory}
              onChange={(option: Category | null) => {
                setUploadData((prevState) => {
                  return { ...prevState, productCategory: option.name };
                });
                setProductState({ selectedCategory: option });
              }}
              getOptionLabel={(category: Category) => category.name}
              getOptionValue={(category: Category) => category.name}
              options={categories}
              isClearable={true}
              backspaceRemovesValue={true}
              placeholder={uploadData.productCategory}
            />

          </div>
          <div className={styles.categoryBox}>
            <p className={styles.star}>*</p>
            <p className={styles.categoryText2}>원하는 물건의 카테고리를 선택해주세요.</p>
          </div>
          <div className={styles.categoryBox2}>
            <Select
              styles={{ // zIndex
                menu: provided => ({ ...provided, zIndex: 999 }),
              }}
              // If you don't need a state you can remove the two following lines value & onChange
              value={wishState.selectedCategory}
              onChange={(option: Category | null) => {
                setUploadData((prevState) => {
                  return { ...prevState, wishCategory: option.name };
                });
                setWishState({ selectedCategory: option });
              }}
              getOptionLabel={(category: Category) => category.name}
              getOptionValue={(category: Category) => category.name}
              options={categories2}
              isClearable={true}
              backspaceRemovesValue={true}
              placeholder={uploadData.wishCategory}
            />

          </div>

          <Tags
            className={styles.customLook}
            placeholder='해시태그를 적고 엔터를 눌러주세요.'
            //여기서 자동완성을 설정할수있음, 추후에 서버에서 tag 리스트를 가져와서 넣으면 될듯
            whitelist={['스팸', '식품', '과일존맛', '신상품', '스팸클래식', '이게자동완성이라는건데요']}
            // defaultValue="a,b,c"
            onChange={onChange}
            value={uploadData.tag}
          />


        </div>

        <div className={styles.btnPlace}>
          <button className={styles.uploadBtn} onClick={onClickUploadButton}>내 물건 올리기</button>
        </div>

      </div>

    </div>


  );

};

export default PostEdit;