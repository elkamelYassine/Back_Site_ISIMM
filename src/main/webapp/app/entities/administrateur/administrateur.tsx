import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, reset } from './administrateur.reducer';

export const Administrateur = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const administrateurList = useAppSelector(state => state.administrateur.entities);
  const loading = useAppSelector(state => state.administrateur.loading);
  const links = useAppSelector(state => state.administrateur.links);
  const updateSuccess = useAppSelector(state => state.administrateur.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="administrateur-heading" data-cy="AdministrateurHeading">
        <Translate contentKey="isimmManagerApp.administrateur.home.title">Administrateurs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="isimmManagerApp.administrateur.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/administrateur/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="isimmManagerApp.administrateur.home.createLabel">Create new Administrateur</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={administrateurList ? administrateurList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {administrateurList && administrateurList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="isimmManagerApp.administrateur.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('prenom')}>
                    <Translate contentKey="isimmManagerApp.administrateur.prenom">Prenom</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('prenom')} />
                  </th>
                  <th className="hand" onClick={sort('nom')}>
                    <Translate contentKey="isimmManagerApp.administrateur.nom">Nom</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('nom')} />
                  </th>
                  <th className="hand" onClick={sort('email')}>
                    <Translate contentKey="isimmManagerApp.administrateur.email">Email</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                  </th>
                  <th className="hand" onClick={sort('matricule')}>
                    <Translate contentKey="isimmManagerApp.administrateur.matricule">Matricule</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('matricule')} />
                  </th>
                  <th className="hand" onClick={sort('titre')}>
                    <Translate contentKey="isimmManagerApp.administrateur.titre">Titre</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('titre')} />
                  </th>
                  <th className="hand" onClick={sort('numTel')}>
                    <Translate contentKey="isimmManagerApp.administrateur.numTel">Num Tel</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('numTel')} />
                  </th>
                  <th className="hand" onClick={sort('dateDeNaissance')}>
                    <Translate contentKey="isimmManagerApp.administrateur.dateDeNaissance">Date De Naissance</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('dateDeNaissance')} />
                  </th>
                  <th className="hand" onClick={sort('photoDeProfile')}>
                    <Translate contentKey="isimmManagerApp.administrateur.photoDeProfile">Photo De Profile</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('photoDeProfile')} />
                  </th>
                  <th>
                    <Translate contentKey="isimmManagerApp.administrateur.user">User</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {administrateurList.map((administrateur, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/administrateur/${administrateur.id}`} color="link" size="sm">
                        {administrateur.id}
                      </Button>
                    </td>
                    <td>{administrateur.prenom}</td>
                    <td>{administrateur.nom}</td>
                    <td>{administrateur.email}</td>
                    <td>{administrateur.matricule}</td>
                    <td>{administrateur.titre}</td>
                    <td>{administrateur.numTel}</td>
                    <td>
                      {administrateur.dateDeNaissance ? (
                        <TextFormat type="date" value={administrateur.dateDeNaissance} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {administrateur.photoDeProfile ? (
                        <div>
                          {administrateur.photoDeProfileContentType ? (
                            <a onClick={openFile(administrateur.photoDeProfileContentType, administrateur.photoDeProfile)}>
                              <img
                                src={`data:${administrateur.photoDeProfileContentType};base64,${administrateur.photoDeProfile}`}
                                style={{ maxHeight: '30px' }}
                              />
                              &nbsp;
                            </a>
                          ) : null}
                          <span>
                            {administrateur.photoDeProfileContentType}, {byteSize(administrateur.photoDeProfile)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{administrateur.user ? administrateur.user.login : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/administrateur/${administrateur.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/administrateur/${administrateur.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/administrateur/${administrateur.id}/delete`)}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="isimmManagerApp.administrateur.home.notFound">No Administrateurs found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Administrateur;
