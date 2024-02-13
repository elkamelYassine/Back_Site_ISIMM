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

import { getEntities, reset } from './professeur.reducer';

export const Professeur = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [sorting, setSorting] = useState(false);

  const professeurList = useAppSelector(state => state.professeur.entities);
  const loading = useAppSelector(state => state.professeur.loading);
  const links = useAppSelector(state => state.professeur.links);
  const updateSuccess = useAppSelector(state => state.professeur.updateSuccess);

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
      <h2 id="professeur-heading" data-cy="ProfesseurHeading">
        <Translate contentKey="isimmManagerApp.professeur.home.title">Professeurs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="isimmManagerApp.professeur.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/professeur/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="isimmManagerApp.professeur.home.createLabel">Create new Professeur</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={professeurList ? professeurList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {professeurList && professeurList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="isimmManagerApp.professeur.id">ID</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('prenom')}>
                    <Translate contentKey="isimmManagerApp.professeur.prenom">Prenom</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('prenom')} />
                  </th>
                  <th className="hand" onClick={sort('nom')}>
                    <Translate contentKey="isimmManagerApp.professeur.nom">Nom</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('nom')} />
                  </th>
                  <th className="hand" onClick={sort('email')}>
                    <Translate contentKey="isimmManagerApp.professeur.email">Email</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                  </th>
                  <th className="hand" onClick={sort('matricule')}>
                    <Translate contentKey="isimmManagerApp.professeur.matricule">Matricule</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('matricule')} />
                  </th>
                  <th className="hand" onClick={sort('departement')}>
                    <Translate contentKey="isimmManagerApp.professeur.departement">Departement</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('departement')} />
                  </th>
                  <th className="hand" onClick={sort('titre')}>
                    <Translate contentKey="isimmManagerApp.professeur.titre">Titre</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('titre')} />
                  </th>
                  <th className="hand" onClick={sort('numTel')}>
                    <Translate contentKey="isimmManagerApp.professeur.numTel">Num Tel</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('numTel')} />
                  </th>
                  <th className="hand" onClick={sort('dateDeNaissance')}>
                    <Translate contentKey="isimmManagerApp.professeur.dateDeNaissance">Date De Naissance</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('dateDeNaissance')} />
                  </th>
                  <th className="hand" onClick={sort('photoDeProfile')}>
                    <Translate contentKey="isimmManagerApp.professeur.photoDeProfile">Photo De Profile</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('photoDeProfile')} />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {professeurList.map((professeur, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/professeur/${professeur.id}`} color="link" size="sm">
                        {professeur.id}
                      </Button>
                    </td>
                    <td>{professeur.prenom}</td>
                    <td>{professeur.nom}</td>
                    <td>{professeur.email}</td>
                    <td>{professeur.matricule}</td>
                    <td>
                      <Translate contentKey={`isimmManagerApp.Departement.${professeur.departement}`} />
                    </td>
                    <td>{professeur.titre}</td>
                    <td>{professeur.numTel}</td>
                    <td>
                      {professeur.dateDeNaissance ? (
                        <TextFormat type="date" value={professeur.dateDeNaissance} format={APP_LOCAL_DATE_FORMAT} />
                      ) : null}
                    </td>
                    <td>
                      {professeur.photoDeProfile ? (
                        <div>
                          {professeur.photoDeProfileContentType ? (
                            <a onClick={openFile(professeur.photoDeProfileContentType, professeur.photoDeProfile)}>
                              <img
                                src={`data:${professeur.photoDeProfileContentType};base64,${professeur.photoDeProfile}`}
                                style={{ maxHeight: '30px' }}
                              />
                              &nbsp;
                            </a>
                          ) : null}
                          <span>
                            {professeur.photoDeProfileContentType}, {byteSize(professeur.photoDeProfile)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/professeur/${professeur.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/professeur/${professeur.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/professeur/${professeur.id}/delete`)}
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
                <Translate contentKey="isimmManagerApp.professeur.home.notFound">No Professeurs found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Professeur;
