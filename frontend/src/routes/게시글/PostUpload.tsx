import React, { useCallback, useEffect, useRef, useState } from 'react';
import styles from '../../styles/loginAndSignup/PostUpload.module.css';
import { useNavigate } from 'react-router-dom';
import photo from '../../img/add.png';
import Tags from '@yaireo/tagify/dist/react.tagify'; // React-wrapper file
// import "@yaireo/tagify/src/tagify.scss"
import '../../styles/scss/main.scss';
import Api from '../../utils/api';
import { Rootstate } from '../../index';
import { useDispatch, useSelector } from 'react-redux';
//https://github.com/yairEO/tagify 에서 tagify 참조
import { NumericFormat } from 'react-number-format';
import Select from 'react-select';
import Modal from '../로그인 & 회원가입/NeighborModal';
import { TiDelete } from 'react-icons/ti';
import { setToken } from '../../store/jwtTokenReducer';


const PostUpload = () => {
  interface UploadData {
    title: string;
    price: string;
    content: string;
    productCategory: String;
    wishCategory: String;
    tag: string[];
  }

  interface UploadValidationCheck {
    photo: boolean;
    title: boolean;
    price: boolean;
    content: boolean;
    productCategory: boolean;
    wishCategory: boolean;
    tag: boolean;
  }

  const dispatch = useDispatch();

  const [uploadData, setUploadData] = useState<UploadData>();
  let tradeEachOther: boolean = undefined;
  const store = useSelector((state: Rootstate) => state);
  const fileInput = useRef(null);
  const [photoData, setPhotoData] = useState(null);
  const [photoUrl, setPhotoUrl] = useState<string[]>(null);
  let formData;
  const [isOpenModal, setOpenModal] = useState<boolean>(false);

  //중복클릭 업로드 방지
  const [isUploading, setIsUploading] = useState(false);


  const onClickToggleModal = useCallback(() => {
    setOpenModal(!isOpenModal);
  }, [isOpenModal]);

  // const categories: string[] =
  //     ['전체', '도서', '생활가전', '의류', '유아도서', '유아동', '여성의류', '남성의류', '뷰티/미용', '스포츠/레저',
  //         '티켓/교환권', '식물', '가구', '반려동물용품', '가공용품', '취미/게임', '인테리어', '생활/주방']

  interface Category {
    name: string;
  }

  const categories: Category[] =
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
  const [showImages, setShowImages] = useState([]);

  const onChangeTitle = (e) => {
    const inputTitle = e.target.value;
    if (inputTitle.length > 30) {
      console.log('글자 5글자 이상');
      console.log(inputTitle.length);
      alert('제목은 30글자 이하로 작성해주세요');
    } else {
      setUploadData((prevState) => {
        return { ...prevState, title: inputTitle };
      });
    }
  };


  const settings = {
    maxTags: 6,
    delimiters: ',|\n|\r',
    // pattern : /^.{0,9}$/,
  };

  const onChangePrice = (e, value) => {
    const inputPrice = e.target.value;

    setUploadData((prevState) => {
      return { ...prevState, price: inputPrice };
    });
  };

  const onChangeContent = (e) => {
    const inputContent = e.target.value;
    const str_arr = inputContent.split('\n');
    console.log(str_arr);
    console.log(inputContent);
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

    console.log(tagList);

    setUploadData((prevState) => {
      return { ...prevState, tag: tagList };
    });

  }, []);

  async function uploadPost(jsonObj) {

    //interceptor를 사용한 방식 (header에 token값 전달)
    try {

      console.log(jsonObj);
      const res = await Api.post('/post', jsonObj);
      console.log(res);

      alert('업로드 성공');
      navigate(`/`);
    } catch (err) {
      // setIsUploading(false);
      console.log(err);
      alert('업로드 실ㅌ패');
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

    // if (isUploading) {
    //   return;
    // }
    //
    // setIsUploading(true);

    console.log(uploadData);

    if (uploadData.productCategory === uploadData.wishCategory) {
      tradeEachOther = true;

      console.log(tradeEachOther);
      console.log('true');
    } else {
      tradeEachOther = false;
      console.log(tradeEachOther);
      console.log('false');
    }


    //사진 업로드 필수항목 검증
    if (!(uploadData.title &&
      uploadData.tag &&
      uploadData.price &&
      uploadData.content &&
      uploadData.wishCategory &&
      uploadData.productCategory)) {
      alert('업로드 할 내용을 다 채워주세요.');
    } else {
      const photoUrlList = await imageUpload();

      if (photoUrlList.length < 1) {
        alert('사진을 한장 이상 업로드해주세요.');
      } else {
        console.log(photoUrlList);
        const jsonObj = {
          'title': uploadData.title,
          'content': uploadData.content,
          'price': uploadData.price,
          'tradeEachOther': tradeEachOther,
          'authorId': store.userInfoReducer.id,
          'productCategory': uploadData.productCategory,
          'wishCategory': uploadData.wishCategory,
          'tagNames': [...uploadData.tag],
          'images': [...photoUrlList],
          'thumbnail': photoUrlList[0],
        };
        //업로드 유효검증 로직 추가로 짜야함
        uploadPost(jsonObj);

        console.log('업로드 성공');
      }
    }

  };


  const onChangeImg = async (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();

    if (e.target.files) {
      const imageLists = e.target.files;
      let imageUrlLists = [...showImages];
      formData = new FormData();

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
      console.log(showImages);

      //api 통신
      setPhotoData(formData);

      console.log(formData.getAll('imageFiles'));
      console.log(photoData);

    }
  };

  interface ArrayObjectSelectState {
    selectedCategory: Category | null;
  }

  const [productState, setProductState] = React.useState<ArrayObjectSelectState>({
    selectedCategory: null,
  });

  const [wishState, setWishState] = React.useState<ArrayObjectSelectState>({
    selectedCategory: null,
  });


  const onChangePriceSecond = (value) => {
    setUploadData((prevState) => {
      return { ...prevState, price: value };
    });
  };

  // const deleteImg = (index) => {
  //   setShowImages((prevState) => {
  //     prevState.splice(index, 1);
  //     return [...prevState];
  //   });
  //
  //   const newPhotoData = new FormData();
  //   showImages.forEach((image, i) => {
  //     if (i !== index) {
  //       newPhotoData.append('imageFiles', image);
  //     }
  //   });
  //
  //   setPhotoData(newPhotoData);
  // };


  const deleteImg = (index) => {
    setShowImages((prevState) => {
      const updatedImages = [...prevState];
      updatedImages.splice(index, 1);
      return updatedImages;
    });

    setPhotoData((prevState) => {
      const newPhotoData = new FormData();
      prevState.getAll('imageFiles').forEach((file, i) => {
        if (i !== index) {
          newPhotoData.append('imageFiles', file);
        }
      });
      return newPhotoData;
    });
  };

  async function reissue() {

    //interceptor를 사용한 방식 (header에 token값 전달)
    try {
      const jsonObj = { 'accessToken': store.jwtTokenReducer.accessToken };
      const data = await Api.post('/auth/reissue', jsonObj);
      dispatch(setToken(data.data));
    } catch (err) {
      console.log(err);
      alert('get 실패');
    }
  }

  /**
   * 로그인 상태를 확인하고 비로그인일때 이전화면으로 돌아가기
   */
  useEffect(() => {
    if (store.jwtTokenReducer.authenticated) {
      console.log('로그인 상태');
      //토큰 만료되면 reissue걸때 게시글 두개 올라가는 버그 막기 위해 reissue먼저 해줌
      reissue();

    } else {
      alert('로그인후에 가능한 서비스입니다.');
      navigate(-1);
    }
  }, [store.jwtTokenReducer.authenticated]);

  console.log(photoData);


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

                <div className={styles.imgClass}><img className={styles.photos} alt={`${image}-${id}`}
                                                      key={image.id} src={image}
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
                                                   placeholder=' 글 제목을 적어주세요.' onBlur={onChangeTitle} />
          </div>
          <div className={styles.item2}>
            <p className={styles.star}>*</p> <NumericFormat className={styles.item2_2}
                                                            placeholder=' 생각하는 물건의 가격대를 숫자로 적어주세요.'
                                                            prefix={'₩'} allowLeadingZeros
                                                            thousandSeparator=','
                                                            onValueChange={(values) => {
                                                              onChangePriceSecond(values.floatValue);
                                                            }} />
          </div>
          <div className={styles.contentAreaBox}>
            <p className={styles.star}>*</p>
            <textarea className={styles.contentArea} placeholder=' 게시글 본문을 최대 7줄로 작성해주세요.'
                      onBlur={onChangeContent} spellCheck={'false'} />
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
              placeholder={'전체'}
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
              placeholder={'전체'}
            />

          </div>

          <Tags
            className={styles.customLook}
            settings={settings}
            placeholder='최대 6개의 해시태그를 적어주세요.'
            //여기서 자동완성을 설정할수있음, 추후에 서버에서 tag 리스트를 가져와서 넣으면 될듯
            whitelist={['스팸', '식품', '과일존맛', '신상품', '스팸클래식', '이게자동완성이라는건데요']}
            // defaultValue="a,b,c"
            onChange={onChange}
          />


        </div>

        <div className={styles.btnPlace}>
          {/*<button className={styles.uploadBtn} onClick={onClickUploadButton} disabled={isUploading}>*/}
          <button className={styles.uploadBtn} onClick={onClickUploadButton}>
            내 물건 올리기
          </button>
        </div>

      </div>

    </div>


  );

};

export default PostUpload;