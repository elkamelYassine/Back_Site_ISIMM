import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './fichier-admin.reducer';

export const FichierAdmin = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const fichierAdminList = useAppSelector(state => state.fichierAdmin.entities);
  const loading = useAppSelector(state => state.fichierAdmin.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="fichier-admin-heading" data-cy="FichierAdminHeading">
        <Translate contentKey="isimmManagerApp.fichierAdmin.home.title">Fichier Admins</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="isimmManagerApp.fichierAdmin.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/fichier-admin/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="isimmManagerApp.fichierAdmin.home.createLabel">Create new Fichier Admin</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {fichierAdminList && fichierAdminList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="isimmManagerApp.fichierAdmin.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('titre')}>
                  <Translate contentKey="isimmManagerApp.fichierAdmin.titre">Titre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('titre')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="isimmManagerApp.fichierAdmin.type">Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('file')}>
                  <Translate contentKey="isimmManagerApp.fichierAdmin.file">File</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('file')} />
                </th>
                <th className="hand" onClick={sort('demandeValide')}>
                  <Translate contentKey="isimmManagerApp.fichierAdmin.demandeValide">Demande Valide</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('demandeValide')} />
                </th>
                <th>
                  <Translate contentKey="isimmManagerApp.fichierAdmin.etudiant">Etudiant</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fichierAdminList.map((fichierAdmin, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/fichier-admin/${fichierAdmin.id}`} color="link" size="sm">
                      {fichierAdmin.id}
                    </Button>
                  </td>
                  <td>{fichierAdmin.titre}</td>
                  <td>
                    <Translate contentKey={`isimmManagerApp.TypeFichierAdmin.${fichierAdmin.type}`} />
                  </td>
                  <td>
                    {fichierAdmin.file ? (
                      <div>
                        {fichierAdmin.fileContentType ? (
                          <a onClick={openFile(fichierAdmin.fileContentType, fichierAdmin.file)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {fichierAdmin.fileContentType}, {byteSize(fichierAdmin.file)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{fichierAdmin.demandeValide ? 'true' : 'false'}</td>
                  <td>
                    {fichierAdmin.etudiant ? <Link to={`/etudiant/${fichierAdmin.etudiant.id}`}>{fichierAdmin.etudiant.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/fichier-admin/${fichierAdmin.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/fichier-admin/${fichierAdmin.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/fichier-admin/${fichierAdmin.id}/delete`)}
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
              <Translate contentKey="isimmManagerApp.fichierAdmin.home.notFound">No Fichier Admins found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FichierAdmin;
